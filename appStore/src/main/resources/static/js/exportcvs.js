$("#exportcsv1").click(
		function() {
			var url="exportcvs";
	        window.open(url);
		}
	);

$(document).ready(function () {
	//加载otalog
	queryCustomerList();
	queryModelList();
	queryotalog();
	$('#confirm_selectOtaLogWithFilter').bind("click", confirm_selectOtaLogWithFilterClick);
});
function actionReFormFun(value,row,index) {
	if(value=="Box Start") {
		return '<font color="green">Box Start</font>';
	} else {
		return '<font color="red">Box Stop</font>';
	}
}

function confirm_selectOtaLogWithFilterClick() {
    $('#otalog_table').bootstrapTable('refresh');
}


//时间格式转换
function timeFormatter(value,row,index) {
	var date = new Date(value);
	var updateTime = date.getFullYear() +"-"+ (parseInt(date.getMonth()) + 1) +"-"+ date.getDate()+" " +date.getHours() + ":" +date.getMinutes()+":"+date.getSeconds();
	return updateTime;
}

function queryotalog() {
	$('#otalog_table').bootstrapTable ({
    	height:520,
    	//search:true,
//    	showRefresh:true,
//    	showToggle:true,
    	//showColumns:true,
        pagination: true,
        showExport:true,
        exportDataType:'all',
        sidePagination: 'server',
        
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50],
//        toolbar: $('#toolbar'),
        uniqueId: 'id',
        queryParams: function (params) {
            return {
            	keyword: $('#keyword').val(),
                customer: $('#selectCustomer').val(),
                model: $('#selectModel').val(),
                begintime: $('#selectBegintime').val(),
                endtime: $('#selectEndtime').val(),
                
                pageSize: params.limit,
                offset: params.offset
            }
        },
        url: 'box-info',
    });
}

function queryCustomerList() {
    $.ajax({
        type: "get",
        url: "customer-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
                $('#selectCustomer').append("<option value=" + data[i].id + ">" + data[i].name + "</option>");
            }
            $('#selectCustomer').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}

function queryModelList() {
    $.ajax({
        type: "get",
        url: "model-info",
        data: {
            //
        },
        success: function (result) {
            var data = $.parseJSON(result);
            for (var i = 0; i < data.length; i++) {
            	if(data[i].status == 0) continue;
                $('#selectModel').append("<option value=" + data[i].id + ">" + data[i].type + "</option>");
            }
            $('#selectModel').selectpicker('refresh');
        },
        error: function (result, xhr) {
            console.log(result);
            console.log(xhr);
        }
    });
}


