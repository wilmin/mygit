$(document).ready(function () {
	queryroleinfo();
});

function queryroleinfo() {
	 $('#role_table').bootstrapTable({
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
	        url: 'role-info',
	    });
}
function OperationFun(value,row,index) {
	return ['<button type="button" class="btn btn-info btn-sm accessRole"><span class="glyphicon glyphicon glyphicon-access"></span>Access</button>&nbsp;',
	        '<button type="button" class="btn btn-success btn-sm editRole"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>&nbsp;',
	        '<button type="button" class="btn btn-danger btn-sm deleteRole"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>'].join('');	
}

function Reform(value,row,index) {
	return value;
}
window.actionEvents = {
        'click .deleteRole': function (e, value, row, index) {
        	layer.msg('Sure to delete this record?', {
        		  time: 0
        		  ,btn: ['Confirm', 'Cancel']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"role":row.role,
             			}
                	$.ajax({
                		type: "post",
                		url: "deleteRole",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			result = $.parseJSON(result);
                			if (result.code == 1) {
                				layer.msg('Delete Successfully!');
                				$('#role_table').bootstrapTable('refresh');
                			} else if(result.code == 0){
                				layer.alert('Can not delete this role because this role is occupied by some user!',{icon:2});
                			}
                		},
                		error: function(result, xhr) {
                			console.log(result);
                			console.log(xhr);
                		}
                	})

        		  }});

        	
        },
        'click .editRole': function (e, value, row, index) {
        	$('#editRoleModal').modal('show');
        	$('#editRole').val(row.role);
        	$('#editRole').attr("disabled",true);
        },
        
        'click .accessRole': function (e, value, row, index) {
        	$('#edit_Role').text(row.role);
        	var htmlOut = '<div style="border:1px solid #DDDDDD;">'
        		+ '<div style="background-color:#DDDDDD;">&nbsp;#TITLE#</div>'
        		+ '#CONTENT#</div><br/>';
        	var htmlInSide = '<label class="checkbox-inline">'
        		+'&nbsp;<input name="buttonIds" type="checkbox" #CHECKED# value="#VALUE#"> #BUTTON_NAME#'
        		+'</label>';
        	var html = '';
        	$.post("bottonMap", {"roleId" : row.role}, function(data) {
        		if(data == null || data == undefined || data == "") alert("沒有添加按钮数据！");
        		data = $.parseJSON(data);
        		for(var key in data) {
        			var dataOne = data[key];
        			var contentHtml = '';
        			for(var j = 0; j < dataOne.length; j++) {
        				var buttonOne = dataOne[j];
        				contentHtml += htmlInSide.replace("#VALUE#", buttonOne.id).replace("#BUTTON_NAME#", buttonOne.name).replace("#CHECKED#", buttonOne.checked ? "checked" : "");
        			}
        			html += htmlOut.replace("#TITLE#", key).replace("#CONTENT#", contentHtml);
        		}
        		$("#form-horizontal-data").html('<input type="hidden" value="' + row.role + '" name="roleId" /> ' + html);
        		$('#accessRoleModal').modal('show');
        	});
        }
}
$("#access_submit").click(function() {
	$("#form-horizontal-data").submit();
	layer.msg('Success');
});


$("#submit").click(function() {
	
	var vdevice = $("input[name='vdevice']:checked").val();
	var vgroup = $("input[name='vgroup']:checked").val();
	var vproduct = $("input[name='vproduct']:checked").val();
	var votaupgrade = $("input[name='votaupgrade']:checked").val();
	var votalog = $("input[name='votalog']:checked").val();
	var vappupgrade = $("input[name='vappupgrade']:checked").val();
	var vapplog = $("input[name='vapplog']:checked").val();
	var vadvertisement = $("input[name='vadvertisement']:checked").val();
	var vburnsn = $("input[name='vburnsn']:checked").val();
	var vexportcsv = $("input[name='vexportcsv']:checked").val();
	var vcustomer = $("input[name='vcustomer']:checked").val();
	var vipblock = $("input[name='vipblock']:checked").val();
	if ($("#role").val()) {
		var data = {
			"role": $("#role").val(),
			"vdevice": vdevice,
			"vgroup": vgroup,
			"vproduct": vproduct,
			"votaupgrade": votaupgrade,
			"votalog": votalog,
			"vappupgrade": vappupgrade,
			"vapplog": vapplog,
			"vadvertisement": vadvertisement,
			"vburnsn": vburnsn,
			"vexportcsv": vexportcsv,
			"vcustomer": vcustomer,
			"vipblock": vipblock,
		}
		$.ajax({
			type: "post",
			url: "check-role",
			contentType : 'application/json',
			dataType:"json",
			data: JSON.stringify(data),
			success: function(result) {
				if (result.code != 1) {
					layer.alert("This role is existed!");
					$("#role").focus();
				} else {
					$.ajax({
						type: "post",
						url: "add-role",
						contentType : 'application/json',
						data: JSON.stringify(data),
						success: function(result) {
							layer.alert("Success!");
							$('#addRoleModal').modal('hide');
							$('#role_table').bootstrapTable('refresh');
						},
						error: function(result, xhr) {
							console.log(result);
							console.log(xhr);
							layer.alert("ERROR");
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
		layer.alert("Please complete this form!",{icon:2})
	}
});

$("#editSubmit").click(function() {
	var vdevice = $("input[name='editvdevice']:checked").val();
	var vgroup = $("input[name='editvgroup']:checked").val();
	var vproduct = $("input[name='editvproduct']:checked").val();
	var votaupgrade = $("input[name='editvotaupgrade']:checked").val();
	var votalog = $("input[name='editvotalog']:checked").val();
	var vappupgrade = $("input[name='editvappupgrade']:checked").val();
	var vapplog = $("input[name='editvapplog']:checked").val();
	var vadvertisement = $("input[name='editvadvertisement']:checked").val();
	var vburnsn = $("input[name='editvburnsn']:checked").val();
	var vexportcsv = $("input[name='editvexportcsv']:checked").val();
	var vcustomer = $("input[name='editvcustomer']:checked").val();
	var vipblock = $("input[name='editvipblock']:checked").val();
	if ($("#editRole").val()) {
		var data = {
			"role": $("#editRole").val(),
			"vdevice": vdevice,
			"vgroup": vgroup,
			"vproduct": vproduct,
			"votaupgrade": votaupgrade,
			"votalog": votalog,
			"vappupgrade": vappupgrade,
			"vapplog": vapplog,
			"vadvertisement": vadvertisement,
			"vburnsn": vburnsn,
			"vexportcsv": vexportcsv,
			"vcustomer": vcustomer,
			"vipblock": vipblock,
		}
		$.ajax({
			type: "post",
			url: "edit-role",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				layer.alert("Success!");
				$('#editRoleModal').modal('hide');
				$('#role_table').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.alert("ERROR");
			}
		});
	} else {
		layer.alert("Please complete this form!",{icon:2})
	}
});
