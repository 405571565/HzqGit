$(function() {
	$(".main>div").slimScroll({
		width: 'auto', //可滚动区域宽度
		height: '100%', //可滚动区域高度
		opacity: .3, //滚动条透明度
	});
	$(".sideNav>ul").slimScroll({
		width: '270px', //可滚动区域宽度
		height: '100%', //可滚动区域高度
		opacity: .3, //滚动条透明度
	});
});
var url = location.href.split('SMSmanage')[0]
var header = ''
var admin_header = ''
if(localStorage.getItem('role')=='1'){
	admin_header = '<li><a href="changePass.html">修改密码</a></li>'
}
header = '<div class="lfloat logo">\
				<img src="images/logo.png"/>\
			</div>\
			<div class="rfloat">\
				<ul>\
					<li>您好，<i></i></li>'
					+admin_header
					+'<li class="exit">退出</li>\
				</ul>\
			</div>'
$(".header").html(header)
var sideNav = ''
var admin_sideNav = ''
if(localStorage.getItem('role')=='1'){
	admin_sideNav = '<li class="account"><a href="account.html">用户管理</a></li>'
}
sideNav = '<ul>\
				<li class="index"><a href="index.html">首&emsp;页</a></li>'
				+admin_sideNav
				+'<li class="send"><a href="send.html">发送记录</a></li>\
				<li class="pay"><a href="pay.html">充值记录</a></li>\
			</ul>'
$(".sideNav").html(sideNav)
var localUrl = location.href.split('SMSmanage/')[1].split('.html')[0]
$(".sideNav").find('.'+localUrl).addClass('on')
$('.header .rfloat ul li i').text(localStorage.getItem('username'))
$(".exit").click(function(){
	$.ajax({
		type:"post",
		url:url+"LeaveLogin",
		async:true,
		success:function(res){
			if(res=='true'){
				location.href = 'login.html'
			}
		}
	});
})
