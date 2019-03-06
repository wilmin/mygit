$(document).ready(function () {
	
	/*
	$.ajax({
		type: "get",
		url: "getIP",
		data: {
			//
		},
		success: function(result) {
			result = $.parseJSON(result);
			if (result.code == 1) {
				$("#ipServer").val(result.serverip);
			}
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
	$("#home").click(function(){
		//getUserName();

	});
	*/
});
$("#IPChange").click(function() {
	//判断状态，0为读状态，1为写状态
	var status = $(this).attr("data-status");
	if (status === "0") {
		$("#ipServer").prop("readonly", false);
		status = "1";
	} else {
		var data = {
			ip: $("#ipServer").val()
		};
		$.ajax({
			type: "post",
			url: "setIP",
			contentType : "application/json",
			data: JSON.stringify(data),
			success: function(result) {
				if (result.code == 1) {
					alert("Change IP successfully!");
					$("#ipServer").prop("readonly", true);
				}
				status = "0";
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
			}
		});
	}
	$(this).attr("data-status", status);
})
function getUserName() {
	$.ajax({
		type: "get",
		url: "welcome",
		data: {
			//
		},
		success: function(result) {
			if (result.code == 1) {
				$("#welcome").text(result.business_name);
			}
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
}
