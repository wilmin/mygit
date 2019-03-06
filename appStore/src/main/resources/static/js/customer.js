$(document).ready(function () {
	querycustomerinfo();
	//querybusiness();
});


function querycustomerinfo() {
	 $('#customer_table').bootstrapTable({
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
	        url: 'customer-info',
	    });
}
function operationFun(value,row,index) {
	return ['<button type="button" class="btn btn-info btn-sm editCustomer" data-toggle="modal" data-target="editModal"><span class="glyphicon glyphicon glyphicon-edit"></span>编辑</button>',
	        '\&nbsp;\&nbsp;<button type="button" class="btn btn-danger btn-sm deleteCustomer"><span class="glyphicon glyphicon glyphicon-trash"></span>删除</button>'].join('');	
}


window.operationEvents = {
        'click .deleteCustomer': function (e, value, row, index) {
        	layer.msg('确定要删除这条信息吗?', {
        		  time: 0 //不自动关闭
        		  ,btn: ['确认', '取消']
        		  ,yes: function(index){
        		    layer.close(index);
        		    var data = {
             				"id":row.id,
             			}
                	$.ajax({
                		type: "post",
                		url: "delete-customer",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
//                			result = $.parseJSON(result);
                			if (result.code == 1) {
                				layer.msg('删除成功!');
                				$('#addModal').modal('hide');
                				$('#customer_table').bootstrapTable('refresh');
                			}
                		},
                		error: function(result, xhr) {
                			console.log(result);
                			console.log(xhr);
                		}
                	})
        	}});

        	
        },
        'click .editCustomer': function (e, value, row, index) {
        	$('#editModal').modal('show');
        	$("#editCustomer").val(row.name);
        	$("#editnation").val(row.nation);
        	$("#editServerIP").val(row.serverip);
        	$("#customerid").val(row.id);
        }
}
function validateIPaddress( ip ) {  
    if( /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test( ip ) ) {  
        return true;
    }  
    return false;
}

//注册用户
$("#submit").click(function() {
	if ($("#addcustomer").val()) {
		if (!($("#addnation").val())) {
			layer.alert("请填写所在区域!",{icon:2});
			return;
		}
		var data = {
				"name": $("#addcustomer").val(),
				"serverip": $("#addserverip").val(),
				"nation": $("#addnation").val(),
			}
			//首先检查用户名是否重复
			$.ajax({
				type: "post",
				url: "check-newcustomer",
				contentType : 'application/json',
				dataType:"json",
				data: JSON.stringify(data),
				success: function(result) {
					if (result.code != 1) {
						alert("客户名已存在!");
						$("#addcustomer").focus();
					} else {
						$.ajax({
							type: "post",
							url: "add-customer",
							contentType : 'application/json',
							data: JSON.stringify(data),
							success: function(result) {
								$('#addModal').modal('hide');
								$('#customer_table').bootstrapTable('refresh');
							},
							error: function(result, xhr) {
								console.log(result);
								console.log(xhr);
								alert("错误");
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
			layer.alert("请填写客户名称!",{icon:2})
	}
});
//当弹框隐藏后，清空input
$(".accountModal").on("hidden.bs.modal", function() {
	$(".accountModal input").val("");
});

//点击编辑提交按钮
$("#editSubmit").click(function() {
 	if ($("#editCustomer").val()) {
 		/*if(validateIPaddress($("#editserverip").val())) {
 			
 		} else {
 			layer.alert("Illegal IP");
 		}*/
 		if (!($("#editnation").val())) {
			layer.alert("请填写所在区域!",{icon:2});
			return;
		}
			var data = {
					"id": $("#customerid").val(),
					"name": $("#editCustomer").val(),
					"serverip": $("#editserverip").val(),
					"nation": $("#editnation").val(),
				}
				$.ajax({
					type: "post",
					url: "update-customer",
					contentType : 'application/json',
					data: JSON.stringify(data),
					success: function(result) {
						$('#editModal').modal('hide');
						$('#customer_table').bootstrapTable('refresh');
					},
					error: function(result, xhr) {
						console.log(result);
						console.log(xhr);
						alert("错误");
					}
				});
		
	} else {
			layer.msg("请填写客户名称!",{icon:2})
	}
});
