<!--
/*

showPages v1.1
=================================

Infomation
----------------------
Author : Lapuasi
E-Mail : lapuasi@gmail.com
Web : <a href="http://www.lapuasi.com" target="_blank">http://www.lapuasi.com</a>
Date : 2005-11-17


Example
----------------------
var pg = new showPages('pg');
pg.pageCount = 12; //定义总页数(必要)
pg.argName = 'p';    //定义参数名(可选,缺省为page)
pg.printHtml();        //显示页数


Supported in Internet Explorer, Mozilla Firefox
*/
 
function showPages(_docCount, _nPageCount, _nCurrIndex, _sPageName, _sPageExt, _pageSize) { //初始化属性
	this.name = "pg";      //对象名称
	this.page = parseInt(_nCurrIndex, 10) || 1;         //当前页数
	this.pageCount = parseInt(_nPageCount, 10) || 1;    //总页数
	this.argName = 'page'; //参数名
	this.showTimes = 1;    //打印次数
	this.docCount = _docCount || 0;
	this.pageName = _sPageName || "index";
	this.pageExt = _sPageExt || "htm";
	this.pageSize = _pageSize || 20;
}

showPages.prototype.getPage = function(){ //丛url获得当前页数,如果变量重复只获取最后一个
	var args=location.pathname;
	var reg = new RegExp('.*' + this.pageName + '[\_](.*)[\.]'+this.pageExt, 'gi');
	var chk = args.match(reg);
	if(chk==null){this.page=1;}
	else{this.page = parseInt(RegExp.$1)+1;}	
}
showPages.prototype.checkPages = function(){ //进行当前页数和总页数的验证
	if (isNaN(parseInt(this.page))) this.page = 1;
	if (isNaN(parseInt(this.pageCount))) this.pageCount = 1;
	if (this.page < 1) this.page = 1;
	if (this.pageCount < 1) this.pageCount = 1;
	if (this.page > this.pageCount) this.page = this.pageCount;
	this.page = parseInt(this.page);
	this.pageCount = parseInt(this.pageCount);
}
showPages.prototype.createHtml = function(mode){ //生成html代码
	var strHtml = '', prevPage = this.page - 1, nextPage = this.page + 1;
	if (mode == '' || typeof(mode) == 'undefined') mode = 0;
	switch (mode) {
		case 0 : //模式1 (页数,首页,前页,后页,尾页)
			strHtml += '<div class="fy_list">';
			if (prevPage < 1) {
				strHtml += '<span class="disabled">上一页</span>';
			} else {
				strHtml += '<a href="javascript:' + this.name + '.toPage(' + prevPage + ');" class="bor_l1">&lt;&nbsp;上一页</a>';
			}
			if (this.page != 1) strHtml += '<a title="第1页" href="javascript:' + this.name + '.toPage(1);">1</a>';
			if (this.page >= 4) strHtml += '<a>...</a>';
			if (this.pageCount > this.page + 2) {
				var endPage = this.page + 2;
			} else {
				var endPage = this.pageCount;
			}
			for (var i = this.page - 2; i <= endPage; i++) {
				if (i > 0) {
					if (i == this.page) {
						strHtml += '<span title="第' + i + '页" class="current">' + i + '</span>';
					} else {
						if (i != 1 && i != this.pageCount) {
							strHtml += '<a title="第' + i + '页" href="javascript:' + this.name + '.toPage(' + i + ');">' + i + '</a>';
						}
					}
				}
			}
			if (this.page + 3 < this.pageCount) strHtml += '<a>...</a>';
			if (this.page != this.pageCount) strHtml += '<a title="第' + this.pageCount + '页" href="javascript:' + this.name + '.toPage(' + this.pageCount + ');">' + this.pageCount + '</a>';
			if (nextPage > this.pageCount) {
				strHtml += '<span class="disabled">下一页</span>';
			} else {
				strHtml += '<a href="javascript:' + this.name + '.toPage(' + nextPage + ');">下一页&nbsp;&gt;</a>';
			}
			strHtml +='<span>跳转到第&nbsp; ';
			strHtml +='<input id="CP" name="CP" type="text" class="inp6" placeholder="'+this.page+'">&nbsp;&nbsp;页&nbsp;&nbsp;</span><span class="go" onclick="' + this.name + '.toGoPage(CP.value);">GO</span>';				
			strHtml += '<div class="clear"></div>';
			strHtml +='</div>';
			break;		
	}
	return strHtml;
}
showPages.prototype.createUrl = function (page) { //生成页面跳转url
	if (isNaN(parseInt(page))) page = 1;
	if (page < 1) page = 1;
	if (page > this.pageCount) page = this.pageCount;
	if(page <= 1){
	   return this.pageName + "." +  this.pageExt;
	}else{
		return this.pageName + "_" + (page-1) + "." +  this.pageExt;
	}
}
showPages.prototype.toPage = function(page){ //页面跳转
	var turnTo = 1;
	if (typeof(page) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
	} else {
		turnTo = page;
	}
	self.location.href = this.createUrl(turnTo);
}


showPages.prototype.toGoPage = function(_pageNo){ //页面跳转
	var turnTo = 1;
	if (typeof(_pageNo) == 'object') {
		turnTo = page.options[page.selectedIndex].value;
		self.location.href = this.createUrl(turnTo);
		return true;
	} else {
		turnTo = _pageNo;
		if(!(this.IsAllNumeric(turnTo))){
			alert("请填写数字。");
			return false;
	  } 
	  else{
		if(_pageNo>this.pageCount ||_pageNo==0){
				alert("请填写在总页数范围内的数字。");
				return false;
		}		
		else{
			turnTo = _pageNo;
			self.location.href = this.createUrl(turnTo);
			return true;  
		}
	  }  
	}
	
}


showPages.prototype.IsAllNumeric = function(number){ //页面跳转
	  var l=number.length;
	  var i,s;
	  for(i=0;i<l;i++)
	  { s=number.charAt(i);
		if(!(s>='0'&&s<='9')) return(false);
	  }
	  return(true);
}

showPages.prototype.printHtml = function(mode){ //显示html代码
	this.getPage();
	this.checkPages();
	this.showTimes += 1;
	//判断页数是否大于2
	if(this.pageCount>1){
	  document.write('<div id="pages_' + this.name + '_' + this.showTimes + '" class="digg"></div>');
	  document.getElementById('pages_' + this.name + '_' + this.showTimes).innerHTML = this.createHtml(mode);
	}
	
}
showPages.prototype.formatInputPage = function(e){ //限定输入页数格式
	var ie = navigator.appName=="Microsoft Internet Explorer"?true:false;
	if(!ie) var key = e.which;
	else var key = event.keyCode;
	if (key == 8 || key == 46 || (key >= 48 && key <= 57)) return true;
	return false;
}

//-->