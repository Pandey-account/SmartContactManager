package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.helper.Message;
import com.smart.models.Contact;
import com.smart.models.User;
import com.smart.repository.ContactRepo;
import com.smart.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo repo;
	@Autowired
	private ContactRepo contactRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		
		String userName = principal.getName();
		System.out.println("UserName "+userName );
//		get the user using username
		
		User user = repo.getUserByUserName(userName);
		System.out.println("User "+ user);
	
		m.addAttribute("user", user);
		
	}
	
	@GetMapping("/index")
	public String dashboard(Model m,Principal principal) {
		
		m.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
//	open add from handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model m) {
		
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	//processing add contact form
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("imageFile") MultipartFile image,
			Principal principal,HttpSession session) {
		
		try {
			
			String name = principal.getName();
			User user = this.repo.getUserByUserName(name);
			
			//porcessing and uploading file..
			if(image.isEmpty()) {
				//if the image is empty then try our message
				System.out.println("File is Empty");
				contact.setImage("contact.png");
			}else {
				//image the file to folder and update the name to contact
			     contact.setImage(image.getOriginalFilename());
			     File file = new ClassPathResource("static/Image").getFile();
			     Path path = Paths.get(file.getAbsolutePath()+File.separator+image.getOriginalFilename());
			     Files.copy(image.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			     
			     System.out.println("Image is uploaded");
			}
			contact.setUser(user);
			
			user.getContacts().add(contact);
			
			this.repo.save(user);
			
			System.out.println("Added to data base");
//			message success
			session.setAttribute("message", new Message("your contact is added !! and more..","success"));
			
		} catch (Exception e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace();
//			message error
			session.setAttribute("message", new Message("Something went wrong ! try again..","danger"));
		}
		
		return "normal/add_contact_form";
		
	}
	 
//	show contacts handler
//	per page=5[n]
//	current page = 0 [page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal p) {
		
		String name = p.getName();
		User u = this.repo.getUserByUserName(name);
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts =  this.contactRepo.findContactsByUser(u.getId(),pageable);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("CurrentPage", page);
		m.addAttribute("totalPage", contacts.getTotalPages());
		m.addAttribute("title", "Show User Contacts");
		return "normal/show_contacts";
	}
	
//	showing perticular contact detail
	@GetMapping("/contact/{cId}")
	public String showContactDetails(@PathVariable("cId") Integer cId,Model m,Principal p) {
		
		String userName = p.getName();
		User user = this.repo.getUserByUserName(userName);
		
		System.out.println("cId "+ cId);
		Optional<Contact> conOptional = this.contactRepo.findById(cId);
		Contact con = conOptional.get();
		
		if(user.getId()==con.getUser().getId()) { 
		
			m.addAttribute("contact", con);
		    m.addAttribute("title", con.getName());
		}
		
		return "normal/contact_detail";
	}
	
	//delete contact handler 
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Model m, Principal principal,HttpSession session) {
		
		Optional<Contact> contactOptional = this.contactRepo.findById(cId);
		Contact contact = contactOptional.get();
		
		
	    String userName = principal.getName();
	    User user = this.repo.getUserByUserName(userName);
		
	   try {
		   
		 //remove
		    //image
//		    contact.getImage()
		    File deleteFile = new ClassPathResource("static/Image").getFile();
			File file1 = new File(deleteFile, contact.getImage());
			file1.delete();
		    
			if(user.getId()==contact.getUser().getId()) {
				
				user.getContacts().remove(contact);
				this.repo.save(user);
				session.setAttribute("message",new Message("Contact deleted successfully...","success"));
			}
		
	} catch (Exception e) {
		
		e.printStackTrace();
	}
		return "redirect:/user/show-contacts/0";
	}
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid ,Model m) {
		
		Contact contact = this.contactRepo.findById(cid).get();
		
		m.addAttribute("contact", contact);
		m.addAttribute("title", "Update Contact");
		return "normal/update_form";
	}
	
	//update contact handler
	@RequestMapping(value = "/process-update",method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("imageFile") 
	MultipartFile file , Model m, HttpSession session,Principal principal) {
		
		try {
			
			//old contact details
		    Contact oldContactDetails = this.contactRepo.findById(contact.getcId()).get();
			
			//image..
			if(!file.isEmpty()) {
				//file work
				//rewrite
				
//				delete old photo
				
				File deleteFile = new ClassPathResource("static/Image").getFile();
				File file1 = new File(deleteFile, oldContactDetails.getImage());
				file1.delete();
				
//				update new photo
				
				 File saveFile = new ClassPathResource("static/Image").getFile();
			     Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			     Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			     contact.setImage(file.getOriginalFilename());
			     
			}else {
				contact.setImage(oldContactDetails.getImage());
			}
			
			User user = this.repo.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepo.save(contact);
			session.setAttribute("message",new Message("Your contact is updated...","success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/contact/"+contact.getcId();
	}
	
//	your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		
		m.addAttribute("title", "Profile Page");
		return "normal/profile";
	}
	
	//open settings handler
	@GetMapping("/settings")
	public String openSettings() {
		
		return "normal/settings";
	}
	
//	change password handler
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") 
	String newPassword,Principal p,HttpSession session) {
		
		String userName = p.getName();
		User currentUser = this.repo.getUserByUserName(userName);
		
		if(this.encoder.matches(oldPassword, currentUser.getPassword())) {
			//change the password
			
			currentUser.setPassword(this.encoder.encode(newPassword));
			this.repo.save(currentUser);
			session.setAttribute("message", new Message("Your password is successfully changed...","success"));
			
		}
		else {
//			error message
			session.setAttribute("message", new Message("Please Enter correct old password !!","danger"));
			return "redirect:/user/settings";
		}
		
		System.out.println("oldPassword"+oldPassword);
		
		return "redirect:/user/index";
	
	}
	
	
}



















