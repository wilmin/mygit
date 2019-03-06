$(document).ready(function () {
	//加载用户信息
	$.ajax({
		type: "get",
		url: "user-info",
		data: {
			//
		},
		success: function(result) {
			
			var select = document.getElementById("selectgroup");
			
			var data = $.parseJSON(result);
			var content = "";
			for (var i = 0; i < data.length; i++) {
				
				var option = document.createElement("option");
				option.value = data[i].username;
				option.innerText = data[i].username;
				select.appendChild(option);
			}
			
			
		},
		error: function(result, xhr) {
			console.log(result);
			console.log(xhr);
		}
	});
	//在模态框中预先加载公司
	
});




$("#submit").click(function() {
	var MACBegin = $("#MACBegin").val();
	var MACEnd = $("#MACEnd").val();
	var UserIDBegin = $("#UserIDBegin").val();
	var UserIDEnd = $("#UserIDEnd").val();
	var Group = $("#selectgroup").val();
	if (MACBegin && MACEnd && UserIDBegin && UserIDEnd) {//先判断填写的表单是否为空
		if (checkMACAddress(MACBegin) && checkMACAddress(MACEnd)) {//判断MAC地址是否合法
			var Num0xMACBegin = parseInt(MACBegin.replace(/:/g, ""), 16);
			var Num0xMACEnd = parseInt(MACEnd.replace(/:/g, ""), 16);
			var diffMAC = Num0xMACEnd - Num0xMACBegin;
			var diffUserID = UserIDEnd - UserIDBegin;
			if (diffMAC != diffUserID) {//判断MAC地址和UserID的间隔个数是否相同
				console.log(Num0xMACEnd - Num0xMACBegin);
				console.log(UserIDEnd - UserIDBegin);
				alert("The count between User ID and MAC address is not right!");
			} else if(diffMAC < 0 || diffUserID < 0) {
				alert("MAC address OR UserID should begin with small and end with big");
			} else {
				var count = UserIDEnd-UserIDBegin;
				var data = {
					count: count,
					MACBegin: MACBegin,
					MACEnd: MACEnd,
					UserIDBegin: UserIDBegin,
					UserIDEnd: UserIDEnd,
					Group: Group
				}
				$.ajax({
					contentType: 'application/json',
					type: "post",
					url: "submitBox",
					contentType: 'application/json',
					data:JSON.stringify(data),
					success: function(result) {
						alert("Success, boxes have been added!");
						$("#MACBegin").val("");
						$("#MACEnd").val("");
						$("#UserIDBegin").val("");
						$("#UserIDEnd").val("");
						console.log(result);
					},
					error: function(result) {
						alert("Sorry, adding boxes failed at mac of " + result.erroInMac + "!");
						console.log(result);
					}
				});
			}
		} else {
			alert("The MAC address is illegal！");
		}
	} else {//让空的表单获取焦点
		if (!MACBegin) {
			alert("You don't write MAC Address");
			$("#MACBegin").focus();
		} else if (!MACEnd) {
			alert("You don't write MAC Address");
			$("#MACEnd").focus();
		} else if (!UserIDBegin) {
			alert("You don't write User ID");
			$("#UserIDBegin").focus();
		} else {
			alert("You don't write User ID");
			$("#UserIDEnd").focus();
		}
	}
	return false;
})

function checkMACAddress(address) {
	if (address.length < 17) {
		return false;
	} else {
		var reg = /([a-fA-F0-9]{2}:){5}[a-fA-F0-9]{2}/;
		if (!address.match(reg)) {
			return false;
		}
	}
	return true;
}
