$(document).ready(function () {
	queryuserinfo();
	//querybusiness();
});

//function querybusiness() {
//	$.ajax({
//		type: "get",
//		url: "get-business",
//		data: {
//			//
//		},
//		success: function(result) {
//			var data = $.parseJSON(result);
//			var content = "";
//			for (var i = 0; i < data.length; i++) {
//				content +=
//					'<option value="'+data[i].id+'">'+data[i].name+'</option>';
//			}
//			$("#business").append(content);
//			$("#editbusiness").append(content);
//		},
//		error: function(result, xhr) {
//			console.log(result);
//			console.log(xhr);
//		}
//	});
//}

function queryuserinfo() {
	 $('#user_table').bootstrapTable({
	    	height:720,
	    	search:true,
//	    	showRefresh:true,
//	    	showToggle:true,
	    	showColumns:true,
	        pagination: true,
	        sidePagination: 'client',
	        pageNumber: 1,
	        pageSize: 20,
	        pageList: [10, 20, 30, 50],
//	        toolbar: $('#toolbar'),
	        uniqueId: 'id',
	        url: 'user-info',
	    });
}
function accountOperationFun(value,row,index) {
	return ['<button type="button" class="btn btn-info btn-sm editAccount" data-toggle="modal" data-target="editAccountModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '<button type="button" class="btn btn-danger btn-sm deleteAccount"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>'].join('');	
}


window.actionEvents = {
        'click .deleteAccount': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
        		  time: 0 //不自动关闭
        		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "deleteAccount",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			result = $.parseJSON(result);
                			if (result.code == 1) {
                				layer.msg('Delete Successfully!');
                				$('#user_table').bootstrapTable('refresh');
                			}
                		},
                		error: function(result, xhr) {
                			console.log(result);
                			console.log(xhr);
                		}
                	})
      }});

        	
        },
        'click .editAccount': function (e, value, row, index) {
        	$('#editAccountModal').modal('show');
        	$("#editAccount").val(row.username);
        	$("#editselectRole").val(row.role);
        	$("#userid").val(row.id);
        }
}
$('#editAccountModal').on('show.bs.modal', function () {
	queryEdit_RoleList();
})


$('#addAccountModal').on('show.bs.modal', function () {
	queryAdd_RoleList();
})

function queryAdd_RoleList() {
    $.ajax({
        type: "get",
        url: "role-info",
        data: {
            //
        },
        success: function (result) {
            var select = document.getElementById("addselectRole");
            var data = $.parseJSON(result);
            select.options.length = 0;
            for (var i = 0; i < data.length; i++) {
                $('#addselectRole').append("<option value='" + data[i].role + "'>" + data[i].role + "</option>");
            }
            $('#addselectRole').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

function queryEdit_RoleList() {
    $.ajax({
        type: "get",
        url: "role-info",
        data: {
            //
        },
        success: function (result) {
            var select = document.getElementById("editselectRole");
            var data = $.parseJSON(result);
            select.options.length = 0;
            for (var i = 0; i < data.length; i++) {
                $('#editselectRole').append("<option value='" + data[i].role + "'>" + data[i].role + "</option>");
            }
            $('#editselectRole').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

//注册用户
$("#submit").click(function() {
	if ($("#account").val() && $("#password").val() && $("#password").val() == $("#againPassword").val()) {
		var data = {
			"username": $("#account").val(),
			"password": $("#password").val(),
			"role": $("#addselectRole").val(),
		}
		//首先检查用户名是否重复
		$.ajax({
			type: "post",
			url: "check-account",
			contentType : 'application/json',
			dataType:"json",
			data: JSON.stringify(data),
			success: function(result) {
				if (result.code != 1) {
					alert("The account is existed!");
					$("#account").focus();
				} else {
					$.ajax({
						type: "post",
						url: "add-account",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							$('#addAccountModal').modal('hide');
							$('#user_table').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							alert("ERROR");
						}
					});
				}
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
			}
		});
	} else {
		if ($("#password").val() != $("#againPassword").val()) {
			alert("Please make sure your password is correct!")
		} else {
			alert("Please fill your account or password!")
		}
	}
});
//当弹框隐藏后，清空input
$(".accountModal").on("hidden.bs.modal", function() {
	$(".accountModal input").val("");
});
//点击编辑按钮
function clickEditAccount() {
	var id = $(this).parent().parent().siblings(".id").val();
	var authority = $(this).parent().parent().siblings(".authority")[0].innerHTML;
	var account = $(this).parent().parent().siblings(".account")[0].innerHTML;
	var business = $(this).parent().parent().siblings(".businessName")[0].innerHTML;
	var businessID = $(this).parent().parent().siblings(".businessID").val();
	$('#editAccountModal').modal('show');
	$("#editAccount").val(account);
	$("#editauthority").val(authority);
	$("#userid").val(id);
};

//点击编辑提交按钮
$("#editSubmit").click(function() {
 	if ($("#editPassword").val() && $("#editPassword").val() == $("#editAgainPassword").val()) {
		var data = {
			"id": $("#userid").val(),
			"password": $("#editPassword").val(),
			"role": $("#editselectRole").val(),
		}
		$.ajax({
			type: "post",
			url: "updateAccount",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				$("#editbusiness").children().remove();
				$('#editAccountModal').modal('hide');
				location.href = "usermanagerment.html";
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.alert("ERROR",{icon:2});
			}
		});
	} else {
		if ($("#editPassword").val() != $("#editAgainPassword").val()) {
			layer.msg("Please make sure your password is correct!",{icon:2})
		} else {
			layer.msg("Please fill your account or password!",{icon:2})
		}
	}
});
