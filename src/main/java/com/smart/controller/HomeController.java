package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.helper.Message;
import com.smart.models.Contact;
import com.smart.models.User;
import com.smart.repository.UserRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
	
	@GetMapping("/")
	public String home(Model m) {
		
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model m) {
		
		m.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
		
		m.addAttribute("title", "Register - Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String Register(@Valid @ModelAttribute("user") User user,BindingResult results,
			@RequestParam(value = "agreement",defaultValue = "false") boolean agreement ,
			Model m, HttpSession session) {
		
		try {
			
			if(!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			else if(results.hasErrors()) {
				
				System.out.println("Error "+results.toString());
				m.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
	     	user.setPassword(PasswordEncoder.encode(user.getPassword()));
			
			System.out.println(agreement);
			System.out.println(user);
			
			User result = this.repo.save(user);
			
			m.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered !!" , "alert-success"));
			return "signup";
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went Wrong !!"+e.getMessage(),"alert-danger"));
			
			return "signup";
		}
			
	}
	
//	handler for custom login
	@GetMapping("/sign in")
	public String customLogin(Model model) {
		
		model.addAttribute("title", "Register - Smart Contact Manager");
		return "login";
	}
	
	//update user handler
	@PostMapping("/user/user-update/{id}")
	public String updateUser(@PathVariable("id") Integer id, Model m) {
		
		User u = this.repo.findById(id).get();
		m.addAttribute("user", u);
		m.addAttribute("title", "Update User");
		return "normal/update_user";
	}
	@PostMapping("/user/update-process")
	public String userUpdateProcess(@ModelAttribute User user, @RequestParam("imageFile") MultipartFile file, Model m,HttpSession session) {
		
         
		try {
			 User olduserDetails = this.repo.findById(user.getId()).get();
			 System.out.println(olduserDetails);
			//image..
			if(!file.isEmpty()) {
				//file work
				//rewrite
				
//				delete old photo
				
				File deleteFile = new ClassPathResource("static/Image").getFile();
				File file1 = new File(deleteFile, olduserDetails.getImageUrl());
				file1.delete();
				
//				update new photo
				
				 File saveFile = new ClassPathResource("static/Image").getFile();
			     Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			     Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			     user.setImageUrl(file.getOriginalFilename());
			     
			}else {
				user.setImageUrl(olduserDetails.getImageUrl());
			}
			
			user.setRole("ROLE_USER");
			user.setPassword(olduserDetails.getPassword());
			this.repo.save(user);
			session.setAttribute("message",new Message("User is updated...","success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/profile";
	}
	
	//delete User handler
	@GetMapping("user/user-delete/{id}")
	public String deleteContact(@PathVariable("id") Integer id,HttpSession session) {
		
		User  userDetails = this.repo.findById(id).get();
		
	   try {
		   
		    //remove
		    //image
            //contact.getImage()
		   
		    File deleteFile = new ClassPathResource("static/Image").getFile();
			File file1 = new File(deleteFile, userDetails.getImageUrl());
			file1.delete();
			
			this.repo.deleteById(id);
		   
			session.setAttribute("message",new Message("Contact deleted successfully...","success"));
		
	} catch (Exception e) {
		
		e.printStackTrace();
	}
		return "redirect:/signup";
	}
	
}

