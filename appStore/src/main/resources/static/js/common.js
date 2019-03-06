$(document).ready(function () {
    $("#header").load("header.html");
    $("#sidebar").load("sidebar.html", function () {
    	if(sessionStorage.getItem("sidebar")=="siderbar_device") {
    		if (sessionStorage.getItem("active") == null) {
                $("#index").addClass("active");
                $("#index").parent().parent().parent().addClass("in");
            } else {
                $("#" + sessionStorage.getItem("active")).addClass("active");
                $("#" + sessionStorage.getItem("active")).parent().parent().parent().addClass("in");
            }
    		
    	} else {
    		sessionStorage.setItem("sidebar", "siderbar_device");
    		$('#header_device').addClass="active";
    		$('#header_setting').removeClass="active";
    		$("#index").addClass("active");
            $("#index").parent().parent().parent().addClass("in");
    		
    	}
    	$("#sidebar li").click(function () {
    		sessionStorage.setItem("active", $(this).attr("id"));
    		if ($(this).attr("id") == "logout") {
    			sessionStorage.removeItem("active");
    		}
    	});
    	
        $("#logout").click(function () {
            alert("logout!");
            $.ajax({
                type: "get",
                url: "logout",
                success: function (result) {
                    console.log(result);
                    /*result = $.parseJSON(result);*/
                    if (result.code == 1) {
                        localStorage.clear();
                        sessionStorage.clear();
                        location.href = "login.html";
                    } else {
                        alert("error!");
                    }
                },
                error: function (result, xhr) {
                    console.log(result);
                    console.log(xhr);
                }
            });
        });
    });
});

$('input[type="checkbox"]').click(function () {
    if ($(this).context.checked == false) {
        $("#" + $(this).val()).addClass("displayNone");
        //取消DVB单选框的框选
        if ($("#" + $(this).val()).children().find("input").attr("name") == "DVBSupport") {
            $("#yesSupport").prop("checked", false);
            $("#noSupport").prop("checked", false);
        } else {//如果不是单选框，就删除value值
            $("#" + $(this).val()).children().find("input").val("");
        }
    } else {
        $("#" + $(this).val()).removeClass("displayNone");
    }
});

function header_deviceClick() {
	$('#header_device').addClass=("active");
	$("#index").addClass("active");
	sessionStorage.setItem("active", "index");
    $("#index").parent().parent().parent().addClass("in");
}
//设置模态框,不能通过点击背景让模态框消失
$('.modal').modal({
    backdrop: "static",
    show: false,
});

//截取文件名
function getFileName(filePath) {
    var index = filePath.lastIndexOf('\\') + 1;
    var lastIndex = filePath.lastIndexOf('.');
    if (lastIndex == -1) {
        lastIndex = filePath.length - 1;
    }
    var filename = filePath.substring(index, lastIndex);
    return filename;
}

