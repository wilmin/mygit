$(document).ready(function () {
	//加载用户信息
	queryProductList();
});
window.actionEvents = {
        'click .deleteProduct': function (e, value, row, index) {
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
                		url: "delete-ModelVersion",
                		contentType : 'application/json',
                		data: JSON.stringify(data),
                		success: function(result) {
                			layer.msg(result.msg);
                			$('#product_table').bootstrapTable('refresh');
                		},
                		error: function(result, xhr) {
                			console.log(result);
                			console.log(xhr);
                		}
                	})

      }});

        	
        },
        'click .editProduct': function (e, value, row, index) {
        	$('#editProductModal').modal('show');
        	$('#editProduct_id').val(row.id);
        	$('#editProduct_model').val(row.model);
        	$('#editProduct_android').val(row.android);
        	$('#editProduct_customer').val(row.customer);
        }
}

/***
 * 显示机型版本列表信息
 * @returns
 */
function queryProductList() {
    $('#product_table').bootstrapTable({
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
        url: 'getModelVersion',
    });
}

function productStatusFun(value,row,index) {
	//open是1 close是0
	if(value=="0") {
		return '<font color="red">Close</font>';
	}
	if(value=="1") {
		return '<font color="green">Open</font>';
	}
	return '-';
}
//用于判断是否显示动态按钮
function aaa(){
	// bottons control
	$("button[permission]").each(function(index, event) {
		var permission = $(event).attr("permission");
		$(event).show();
		/*if(checkUserBotton(permission)) {
		}*/
	});
	// end
}

//动态加载按钮
function productOperationFun(value,row,index) {  
	return ['<button type="button" permission="product:edit" class="btn btn-info btn-sm editProduct" data-toggle="modal" data-target="editProductModal"><span class="glyphicon glyphicon glyphicon-edit"></span>edit</button>',
	        '\&nbsp;\&nbsp;<button type="button" permission="product:delete" class="btn btn-danger btn-sm deleteProduct"><span class="glyphicon glyphicon glyphicon-trash"></span>delete</button>',
	        '<script type="text/javascript">aaa();</script>'].join('');	
}


/***
 * 提交机型版本信息
 * @returns
 */
function submit_addProductClick() {
	if ($("#addProduct_model").val()) {
		var data = {
			"model":$('#addProduct_model').val(),
			"android": $('#addProduct_android').val(),
			"customer": $('#addProduct_customer').val()
			/*"status": $('#addProduct_customer').val()*/
		}
		$.ajax({
			type: "post",
			url: "add-ModelVersion",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				layer.msg(result.msg);
				$('#addProductModal').modal('hide');
				$('#product_table').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg('Error');
			}
		});
	} else {
		layer.msg('Please fill in your model!', {icon: 5});
	}
}

//点击编辑提交按钮
$("#editProduct_Submit").click(function() {
 	if ($("#editProduct_model").val()) {
 		var data = {
 				"id":$('#editProduct_id').val(),
 				"model":$('#editProduct_model').val(),
 				"android":$('#editProduct_android').val(),
 				"customer": $('#editProduct_customer').val()
 				/*"status": $('#editProduct_status').val()*/
 			}
		$.ajax({
			type: "post",
			url: "edit-ModelVersion",
			contentType : 'application/json',
			data: JSON.stringify(data),
			success: function(result) {
				$('#editProductModal').modal('hide');
				layer.msg(result.msg);
				$('#product_table').bootstrapTable('refresh');
			},
			error: function(result, xhr) {
				console.log(result);
				console.log(xhr);
				layer.msg('Error',{icon:2});
			}
		});
	} else {
			layer.msg('Please fill in your model!',{icon:2});
	}
});
