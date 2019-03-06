$(document).ready(function () {
	//加载用户信息
	queryIPBlockList();
});
window.actionEvents = {
        'click .deleteIPBlock': function (e, value, row, index) {
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
                		url: "delete-ipblock",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			result = $.parseJSON(result);
                			if (result.code == 1) {
                				layer.msg('Delete Successfully!');
                				$('#ipblock_table').bootstrapTable('refresh');
                			}
                		},
                		error: function(result, xhr) {
                			console.log(result);
                			console.log(xhr);
                		}
                	})
        	}});

        	
        },
        'click .closeIPBlock': function (e, value, row, index) {
        	var data = {
     				"id":row.id,
     			}
        	$.ajax({
        		type: "post",
        		url: "close-ipblock",
        		contentType : 'application/json',
        		data: JSON.stringify(data),
        		success: function(result) {
        			result = $.parseJSON(result);
        			if (result.code == 1) {
        				layer.msg('Close Successfully!');
        				$('#ipblock_table').bootstrapTable('refresh');
        			}
        		},
        		error: function(result, xhr) {
        			console.log(result);
        			console.log(xhr);
        		}
        	})
        },
        'click .editIPBlock': function (e, value, row, index) {
        	$('#editIPBlockModal').modal('show');
        	$('#editIPBlock_id').val(row.id);
        	$('#editIPBlock_ipbegin').val(row.ipbegin);
        	$('#editIPBlock_ipend').val(row.ipend);
        }
}

function queryIPBlockList() {
    $('#ipblock_table').bootstrapTable({
    	height:720,
    	search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	showColumns:true,
        pagination: true,
        sidePagination: 'client',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
//        toolbar: $('#toolbar'),
        uniqueId: 'id',
        url: 'ipblock-info',
    });
}

function ipblockStatusReform(value,row,index) {
	//open是1 close是0
	if(value=="Close") {
		return '<font color="red">Close</font>';
	}
	if(value=="Open") {
		return '<font color="green">Open</font>';
	}
	return '-';
}

function OperationFun(value,row,index) {
	return ['<button type="button" class="btn btn-info btn-sm editIPBlock" data-toggle="modal" data-target="editIPBlockModal"><span class="glyphicon glyphicon glyphicon-edit"></span>Edit</button>',
	        '<button type="button" class="btn btn-primary btn-sm closeIPBlock"><i class="fa fa-close"></i>Close</button>',
	        '<button type="button" class="btn btn-danger btn-sm deleteIPBlock"><span class="glyphicon glyphicon glyphicon-trash"></span>Delete</button>'].join('');	
}

	
function submit_addIPBlockClick() {
	if ($("#addIPBlock_ipbegin").val() && $("#addIPBlock_ipend").val()) {
		if(validateIPaddress($("#addIPBlock_ipbegin").val()) &&  validateIPaddress($("#addIPBlock_ipend").val())) {
			
			if(validateIPAddressBlock($("#addIPBlock_ipbegin").val(), $("#addIPBlock_ipend").val())) {
				var data = {
						"ipbegin":$('#addIPBlock_ipbegin').val(),
						"ipend": $("#addIPBlock_ipend").val(),
						"status": $('#addIPBlock_status').val()
				}
				
				$.ajax({
					type: "post",
					url: "add-ipblock",
					contentType : 'application/json',
					data: JSON.stringify(data),
					success: function(result) {
						$('#addIPBlockModal').modal('hide');
						layer.msg('Success!');
						$('#ipblock_table').bootstrapTable('refresh');
					},
					error: function(result, xhr) {
						console.log(result);
						console.log(xhr);
						layer.msg('Error');
					}
				});	
			} else {
				layer.msg("Please check inputed address block!",{icon:2});
			}
		} else {
			layer.msg("You have entered an invalid IP address!",{icon:2});
		}
	} else {
		layer.msg('Please fill your begin ip and end ip!', {icon: 5});
	}
}

//点击编辑提交按钮
$("#editIPBlock_Submit").click(function() {
 	if ($("#editIPBlock_ipbegin").val() && $("#editIPBlock_ipend").val()) {
 		if(validateIPaddress($("#editIPBlock_ipbegin").val()) &&  validateIPaddress($("#editIPBlock_ipend").val())) {
 			if(validateIPAddressBlock($("#editIPBlock_ipbegin").val(), $("#editIPBlock_ipend").val())) {
 				var data = {
 		 				"id":$('#editIPBlock_id').val(),
 		 				"ipbegin":$('#editIPBlock_ipbegin').val(),
 		 				"ipend": $("#editIPBlock_ipend").val(),
 		 				"status": $('#editIPBlock_status').val()
 		 			}
 				$.ajax({
 					type: "post",
 					url: "update-ipblock",
 					contentType : 'application/json',
 					data: JSON.stringify(data),
 					success: function(result) {
 						$('#editIPBlockModal').modal('hide');
 						layer.msg('Success!');
 						$('#ipblock_table').bootstrapTable('refresh');
 					},
 					error: function(result, xhr) {
 						console.log(result);
 						console.log(xhr);
 						layer.msg('Error',{icon:2});
 					}
 				});
 	 		} else {
 	 			layer.msg("Please check inputed address block!",{icon:2});
 	 		}
 		} else {
 			layer.msg("You have entered an invalid IP address!",{icon:2});
 		}
 		
 		
	} else {
			layer.msg('Please fill begin ip and end ip!',{icon:2});
	}
});
