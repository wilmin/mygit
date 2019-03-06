$(document).ready(function () {
    //第一次进入页面就自动进行一次在线统计
    // if (sessionStorage.getItem("statistic") == null) {
    //     getBox();
    // }
	queryBurnMacSN();
    $('#delete').bind("click", deleteClick);
    $('#confirm_go').bind("click", confirm_goClick);
    $('#submitEdit').bind("click", submitEditClick);
    queryBurnMacSN();
});
/*
 * */
function confirm_goClick() {
    $('#table_burnmacsn').bootstrapTable('refresh');
}

/*
 *       queryBoxList():加载页面时，加载Box信息的函数
 * */
function queryBurnMacSN() {
    $('#table_burnmacsn').bootstrapTable({
    	height:720,
        pagination: true,
        sidePagination: 'server',
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
        toolbar: $('#toolbar'),
        uniqueId: 'mac',
        queryParams: function (params) {
            return {
            	keyword: $('#keyword').val(),
                status: $('#selectStatus').val(),
                pageSize: params.limit,
                offset: params.offset
            }
        },
        url: 'burnmacsn-info',
        //showExport:true,
        onCheck: function () {
            buttonControl('#table_boxinfo', '#add', '#edit', '#delete');
        },
        onCheckAll: function () {
            buttonControl('#table_boxinfo', '#add', '#edit', '#delete');
        },
        onUncheckAll: function () {
            buttonControl('#table_boxinfo', '#add', '#edit', '#delete');
        },
        onUncheck: function () {
            buttonControl('#table_boxinfo', '#add', '#edit', '#delete');
        }
    });
}

function statusReform(value,row,index) {
	if(value=="Success") {
		return '<font color="green">'+'Success'+'</font>';
	} else if(value=="Failed") {
		return '<font color="red">'+'Failed'+'</font>';
	} else if(value=="Waiting"){
		return '<font color="blue">'+'Waiting'+'</font>';
	} else {
		return '-';
	}
	
	
}

/*
 * */
$('#addModal').on('shown.bs.modal', function () {
    // 执行一些动作...
    //alert("coming soon");
})

$("#submitAdd").click(function () {
	if($("#add_mac").val() && $("#add_sn").val() && $("#add_newmac").val() && $("#add_newsn").val()) {
		var mac = $("#add_mac").val();
	    var sn = $("#add_sn").val();
	    var newmac = $("#add_newmac").val();
	    var newsn = $("#add_newsn").val();
	    var data = {
	        mac: mac,
	        sn: sn,
	        newmac: newmac,
	        newsn: newsn,
	    }
	    $.ajax({
	        contentType: 'application/json',
	        type: "post",
	        url: "add-burnmacsn",
	        contentType: 'application/json',
	        data: JSON.stringify(data),
	        success: function (result) {
	            layer.msg("Success, new burn task has been added!");
	            $("#add_mac").val("");
	            $("#add_sn").val("");
	            $("#add_newmac").val("");
	            $("#add_newsn").val("");
	            $('#addModal').modal('hide');
	            $('#table_burnmacsn').bootstrapTable('refresh');
	            
	            console.log(result);
	        },
	        error: function (result) {
	        	layer.alert('Error in add task!',{icon:2});
	            console.log(result);
	        }
	     })
	} else {
		layer.alert('Please complete the relevant information',{icon:7});
	}
    
})
/*
 *
 *       deleteDeviceClick():点击delete时，删除对应的记录
 *
 * */
function deleteClick() {
    var selected = JSON.stringify($('#table_burnmacsn').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if(selected.length > 0) {
    	layer.msg('Sure to delete this record?', {
  		  time: 0 //不自动关闭
  		  ,btn: ['Confirm', 'Cancel']
  		  ,yes: function(index){
  		    layer.close(index);
	  		  for (var i = 0; i < selected.length; i++) {
	              $.ajax({
	                  type: "post",
	                  url: "delete-burnmacsn",
	                  contentType: 'application/json',
	                  data: JSON.stringify({id: selected[i].id}),
	                  success: function (result) {
	                      result = $.parseJSON(result);
	                      if (result.code == 1) {
	                          layer.alert("Delete Successfully!");
	                          $('#table_burnmacsn').bootstrapTable('refresh');
	                      }
	                  },
	                  error: function (result, xhr) {
	                      console.log(result);
	                      console.log(xhr);
	                  }
	              })
	          }
  		  }});
    	
    } else {
    	layer.msg('Please choose a record!',{icon: 5});
    }
    

}
/*
 *
 *
 * */
$('#editModal').on('show.bs.modal', function () {
    // 执行一些动作...
    //alert("coming soon");
    var selected = JSON.stringify($('#table_burnmacsn').bootstrapTable('getSelections'));
    selected = $.parseJSON(selected);
    if (selected.length == 1) {
        $('#edit_mac').val(selected[0].mac);
        $('#edit_id').val(selected[0].id);
        //$("#edit_mac").attr("readOnly", true);
        $('#edit_sn').val(selected[0].sn);
        $('#edit_newmac').val(selected[0].newmac);
        $('#edit_newsn').val(selected[0].newsn);
    } else {
        layer.msg("Please Choose A Record!",{icon:2});
        $('#editModal').modal('hidden');
    }
})

function submitEditClick() {
    data = {
    	id: $('#edit_id').val(),
        mac: $('#edit_mac').val(),
        sn: $('#edit_sn').val(),
        newmac: $('#edit_newmac').val(),
        newsn: $('#edit_newsn').val(),
        status: $('#edit_status').val(),
    };
    $.ajax({
        type: "post",
        url: "edit-burnmacsn",
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            result = $.parseJSON(result);
            if (result.code == 1) {
            	$('#editModal').modal('hide');
            	layer.msg('Success!');
                $('#table_burnmacsn').bootstrapTable('refresh');
            }
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    })
}

