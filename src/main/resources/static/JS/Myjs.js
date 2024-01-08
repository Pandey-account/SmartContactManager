
const toggleSidebar = () => {
	
	if($(".sidebar").is(":visible")){
		
		//true
		//band karana hai
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
		
	}else{
		$(".sidebar").css("display","block");
		$(".content").css("display","none");
	}
};

function deleteUser(id){
	
	swal({
				title: "Are you sure?",
				text: "You want to delete this User..",
				icon: "warning",
				buttons: true,
				dangerMode: true,
			})
				.then((willDelete) => {
					if (willDelete) {
						window.location="/user/user-delete/"+ id;
						
					} else {
						swal("Your user is safe !!");
					}
				});
	
};