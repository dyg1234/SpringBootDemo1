;!(function($){
    $.resource = function(obj){
        var _obj = {
            $id:'list',
            url:'/portal/api/data/',
            classify:{
                action:'statclassfly.xhtml',
                rsName:'',
                status:true
            },
            pagebar:{
                pageIndex:1,
                prepage:5
            },
            themes:[],
            orgs:[],
            industrys:[],
            updates:[],
            dataFormat:[],
            cache:false,
            action:'list.xhtml',
            listArgs:{
              rsName:'rows'
            },
            collect:{
                url:'/portal/u/favorite/fav.xhtml',
                isLogin:false,
                objectType:''
            },
            pgTemp:'',
            searchArgs:{
                chnlId:'',
                searchWords:'',
                subject:'',
                dataType:'',
                industry:'',
                dataFormat:'',
                frequency:'',
                ORDERBY:'',
                ORDERDIR:''
            },
            errorTip:'没有符合条件的数据目录'
        }





        avalon.mix(true, _obj, obj);

        var vmArgs = {
            $id:_obj.$id,
            url:_obj.url + _obj.action,
            isLogin:_obj.collect.isLogin,
            list:[],
            $collect:function(object, e){
                e && e.preventDefault();

                if(!_obj.$vmList.isLogin){
                    terton.login();
                    return false;
                }
                var $target = $(e.target);

                _obj.addCollect(object.$model,function(msg){

                    var status = object.ISFAVORITE ? '取消收藏':'收藏';
                    if(msg.status == 0){
                        alert(status + '失败');
                    }else if(msg.status == 1){
                        $target.removeClass("on").html('收藏');
                    }else if(msg.status == 2){
                        $target.addClass("on").html('取消收藏');
                    }
                });
            },
            $share:function(object, e){
                e && e.preventDefault();
                bdText = object.DOCTITLE;
                bdUrl = object.DOCPUBURL;
                var $target = $(e.target);
                var shareLeft = $target.offset().left + 10;
                var shareTop = $target.offset().top + $target.outerHeight(true);
                console.log($("#sharebox"))
                $("#sharebox").css({
                    top: shareTop + 'px',
                    left: shareLeft + 'px'
                }).show();

            },
            error:false
        }

        _obj.$vmList = avalon.define(vmArgs);

        var vmSearchArgs = {
            $id:_obj.$id + '_search',
            searchForm:_obj.searchArgs,
            industry:_obj.industry,
            subject:_obj.subject,
            chnlId:_obj.chnlId,
            frequency:_obj.frequency,
            dataFormat:_obj.dataFormat,
            sItems:[],
            total:0,
            initArgs:{
                industrys:'industry',

            },
            $setVal:function(key, name , val , e ){
                //key值 name名称 val值
                e && e.preventDefault();

                _obj.upItems(key, name, val);
                _obj.setVmSearchForm( key , val );
                _obj.tosearch();
            },
            $search:function(e){
                e && e.preventDefault();
                var searchWord = _obj.$vmSearch.$model.searchForm.searchWords;
                searchWord = $.trim(searchWord);
                if(searchWord == ''){
                    alert("请输入检索词");
                    return false;
                }
                _obj.upItems('searchWords', searchWord, 'words');
                _obj.tosearch();
            },
            $delSearch:function(key,e){
                e && e.preventDefault();
                _obj.$vmSearch.searchForm[key] = '';
                _obj.upItems(key, 'key', '');
                _obj.tosearch();
            }

        }

        if( _obj.vmSearchArgsExt ){        //根据传入的参数，定义额外附加的vm属性
            for( var a in _obj.vmSearchArgsExt ){
                vmSearchArgs[ a ] = _obj.vmSearchArgsExt[ a ];
            }
        }

        _obj.$vmSearch = avalon.define(vmSearchArgs);


        _obj.vmPageArgs = {
            $id:_obj.$id + '_pagebar',
            pageIndex:~~_obj.pagebar.pageIndex,
            prepage:~~_obj.pagebar.prepage,
            recordCount:'',
            pageCount:'',
            pages:[],
            pgTemp:''+
                '{{pageIndex}}/{{pageCount}}页&ensp;&ensp;&ensp;'+
                '<a ms-if="pageIndex==1" class="disab bor_l1">首页</a>'+
                '<a href="#" ms-if="pageIndex>1" class="bor_l1" ms-click="toPage(1,$event)">首页</a>'+
                '<a ms-if="pageIndex==1" class="disab bor_l1">上一页</a>'+
                '<a href="#" ms-if="pageIndex>1" class="bor_l1" ms-click="toPage(pageIndex-1,$event)">上一页</a>'+
                '<a ms-for="el in pages" href="#" ms-click="toPage(el,$event)" ms-class="{on:el==pageIndex}">{{el}}</a>'+
                '<a href="#" ms-if="pageIndex<pageCount" ms-click="toPage(pageIndex+1,$event)">下一页</a>'+
                '<a ms-if="pageIndex==pageCount" class="disab bor_l1">下一页</a>'+
                '<a ms-if="pageIndex==pageCount" class="disab bor_l1">尾页</a>'+
                '<a href="#" ms-if="pageIndex<pageCount" ms-click="toPage(pageCount,$event)">尾页</a>'+
                '&nbsp;&nbsp;<span>跳转到第&nbsp;<input ms-duplex="inputNumber" type="text" class="inp6">&nbsp;&nbsp;页&nbsp;&nbsp;<img src="/images/19xm_kfpt_gl_go.jpg"  ms-click="toPage(inputNumber,$event)"></span>',
            inputNumber:~~_obj.pagebar.pageIndex,
            toPage:function(index, e){
                (e) && ( e.preventDefault());
                _obj.toPage(index);
            }
        }

        _obj.$vmPage = avalon.define(_obj.vmPageArgs);

        _obj.getClassify = function(){

        }

        _obj.upItems = function(key, name, val){
            if(name == '') return;
            var flag = true;
            var sItems = _obj.$vmSearch.sItems;
            for(var i = 0; i < sItems.length; i++){
                if(sItems[i].type == key){
                    flag = false;
                    if(!val){
                        sItems.splice(i, 1);
                    }else{
                        sItems[i].name = name;
                    }
                }
            }
            if(flag && (val != '' || val != 'all')) _obj.$vmSearch.sItems.push({
                type:key,
                name:name
            })

        }

        _obj.reName = function(arr, val, type){
            var len = arr.length;
            var name = '';
            for(var i = 0; i < len; i++){
                if(arr[i].type == type && arr[i].val == val){
                    name = arr[i].name;
                }
            }
            return name ? name : false

        }

        _obj.setVmSearchForm = function( name , val ){      //  设置vm的searchForm属性，会根据searchArgs的定义，判断取值的有效性
            if(val == 'all' || val == ''){
                _obj.$vmSearch.searchForm[name] = '';
            }else if(val != ''){
                _obj.$vmSearch.searchForm[name] = val;
            }
        };


        _obj.toPage = function(index){
            index = ~~index;

            (index < 1) && (index = 1);

            if( _obj.$vmPage.pageCount > 0 ){
                (index >  _obj.$vmPage.pageCount )&&( index=_obj.$vmPage.pageCount );  //对 大于最大页码的参数，赋值为最大页码
            }

            _obj.$vmPage.pageIndex = index;


            _obj.tosearch(index);
        }





        _obj.createViewPost = function(){

            var searchArgs = _obj.$vmSearch.searchForm.$model;
            var _params = [];
            var ajaxData = {};
            for(var key in searchArgs){
                if(key == 'chnlId'){
                    if(searchArgs[key]) ajaxData.chnlId = searchArgs[key];
                }else if(searchArgs[key] != '' && !/\s+/.test(searchArgs[key])){
                    if(/\d+/.test(searchArgs[key])){
                        _params.push(key + ":" + searchArgs[key])
                    }else if(typeof searchArgs[key] == 'string'){
                        _params.push(key + ":'" + searchArgs[key] + "'")
                    }else if(typeof searchArgs[key] == 'object'){
                        _params.push(key + ":" + JSON.stringify(searchArgs[key]))
                    }

                }
            }

            if(_params.length > 0){
                var b = new Base64();
                var _str = _params.join(',');
                ajaxData.params = b.encode('{' + _str + '}')
            }

            //console.log(ajaxData);

            return ajaxData;
        }


        _obj.getViewData = function(page,callback){
            var sData = _obj.createViewPost();
            sData.pageNo = page;
            sData.pageSize = _obj.$vmPage.prepage;
            var errorTip = _obj.errorTip ? _obj.errorTip:'没有符合条件的数据目录';
            $.ajax({
                url:_obj.$vmList.url,
                data:sData,
                type:'post',
                dataType:'json',
                success:function( data ){
                    if(data && data.success){
                        if(typeof callback == 'function') callback(_obj.dealViewData(data));
                    }else{

                        if(typeof callback == 'function') callback(_obj.dealViewData({error:true, title:errorTip }));
                    }
                },error:function(e){
                    avalon.log(e.status);
                    if(typeof callback == 'function') callback(_obj.dealViewData({error:true, title:errorTip }));
                }
            })
        }

        _obj.dealViewData = function(data){
            var rs = {
                error:false
            }

            if(data.error) return data;

            var s = {};
            if(_obj.type === 'text'){
                var str = $.trim(data);
                str = str.replace(new RegExp("[\n\r]","gm"),"");
                s = (new Function('return'+str))() ;
            }else{
                s = data;
            }
            rs.recordCount = parseInt( s.total , 10) || 0 ;
            rs.pageIndex = parseInt( s.pageNo , 10) || 0;
            rs.pageCount = parseInt( s.pageSize , 10 ) || 0;
            rs.total = parseInt(s.total, 10) || 0;
            rs.list = s[_obj.listArgs.rsName];
            return rs;
        }


        _obj.tosearch = function(page){
            page = page ? page : 1;
            _obj.getViewData(page, function(msg){
                if(!msg.error){
                    var errorTip = _obj.errorTip ? _obj.errorTip:'没有符合条件的数据目录';
                    _obj.$vmList.list = msg.list;
                    _obj.$vmPage.recordCount = msg.recordCount;
                    _obj.$vmPage.pageIndex = msg.pageIndex;
                    _obj.$vmPage.pageCount = Math.ceil(msg.recordCount / _obj.$vmPage.prepage);
                    _obj.$vmSearch.total = msg.total;
                    _obj.pageUtil(msg.pageIndex, Math.ceil(msg.recordCount / _obj.$vmPage.prepage));
                    if(msg.list.length == 0){
                        _obj.$vmList.error = errorTip;
                    }else{
                        _obj.$vmList.error = false;
                    }
                }else{

                    _obj.$vmList.error = msg.title
                }
            });
        }


        _obj.pageUtil = function(index, count) {
            if(index < 1 || index > count) {
                avalon.log("页数格式不正确")
                return false;
            }
            _obj.$vmPage.pageIndex = index;
            _obj.$vmPage.inputNumber = index;
            var minIndex = index - 3;
            var maxIndex = index + 3;
            if(minIndex < 1) {
                if(maxIndex <= count) {
                    maxIndex = maxIndex - minIndex + 1;
                } else {
                    maxIndex = count;
                }
                minIndex = 1;
            } else {
                if(maxIndex > count) {
                    maxIndex = count;
                    minIndex = maxIndex - 6;
                    if(minIndex < 1) {
                        minIndex = 1;
                    }
                }
            }
            var data = [];
            maxIndex = maxIndex > count ? count : maxIndex;
            for(minIndex; minIndex <= maxIndex; minIndex++) {
                data.push(minIndex);
            }
            _obj.$vmPage.pages = data;
            if(typeof callback == 'function') callback(index);

        }


        _obj.dataTreating = function(object, arr){
            var dataArr = [];
            for(var key in object) {
                for (var i = 0; i < arr.length; i++) {
                    if (key == arr[i].value) {
                        arr[i].count = object[key];
                    }
                }
            }

            return arr;
        }

        _obj.addCollect = function(object, callback){
            var ajaxDatas = {};
            if(object.DOCPUBURL == '' || object.DOCTITLE == '' || object.DOCID == ''){
                callback({status:0})
                return false;
            }
            ajaxDatas = {
                docTitle: object.DOCTITLE,
                docPubUrl: object.DOCPUBURL,
                objectId: object.DOCID,
                objectType: _obj.collect.objectType
            }
            var ajaxArgs = {
                url:_obj.collect.url,
                data:ajaxDatas,
                type:'post',
                dataType:'json',
                success:function(msg){
                    //favorite cancel
                    if(msg.result){
                        if(msg.action == 'cancel'){
                            callback({status:1})
                        }else{
                            callback({status:2})
                        }
                    }else{
                        callback({status:0})
                    }
                },
                error:function(){
                    callback({status:0})
                }
            }
            $.ajax(ajaxArgs)

        }


        _obj.init = function(){
            if(_obj.classify.status){
                var ajaxArgs = {
                    url:_obj.url + _obj.classify.action,
                    type:'post',
                    dataType:'json',
                    success:function(msg){
                        if(msg.result){
                            _obj.$vmSearch.subject = _obj.dataTreating(msg.BA_THEME, _obj.$vmSearch.$model.subject);
                            _obj.$vmSearch.chnlId = _obj.dataTreating(msg.BA_ORG, _obj.$vmSearch.$model.chnlId);
                            _obj.$vmSearch.dataFormat = _obj.dataTreating(msg.BA_DATA, _obj.$vmSearch.$model.dataFormat);
                            _obj.$vmSearch.industry = _obj.dataTreating(msg.BA_INDUS, _obj.$vmSearch.$model.industry);
                        }
                    },error:function(){
                        avalon.log('加载分类失败');
                    }
                }
                $.ajax(ajaxArgs);
            }
            var qs = _obj.getQs();
            var type = qs.type;
            if(type && _obj[type]){
                var arr = _obj[type];
                var flag = true;
                var val = qs.value;
                var name = '';
                if(val){
                    for(var i = 0; i < arr.length; i++){
                        if(arr[i].value == val){
                            flag = true;
                            name = arr[i].name;
                        }
                    }
                }
                if(flag){
                    _obj.$vmSearch.searchForm[type] = val;
                    _obj.upItems(type, name, val);

                }
            }
            var searchWords = qs.searchWords;


            if(searchWords){
                searchWords = $.trim(searchWords);
                _obj.$vmSearch.searchForm.searchWords = searchWords;
                _obj.upItems('searchWords', searchWords, 'searchWords');

            }
            _obj.tosearch();
        }

        _obj.getQs = function(){
            //queryNo=150722-5704-010722
            //queryNo=150722-5704-010722&letterId=12232&t=111111111
            if( window.location.search == "") return false ;
            var args = decodeURIComponent( window.location.search.substr(1).replace(/\+/g, '%20')).split("&");  // 替换到参数中的+号
            // var args = decodeURIComponent( window.location.search.substr(1)).split("&");
            var arg = {};
            for( var i = 0 , kLength = args.length  ; i< kLength; i++ ){

                if( /^([^=]+)=(.+)$/.test( args[i] )){
                    var name = RegExp.$1 , value = RegExp.$2 ;
                    if( /^\d/.test( name ) ){
                        avalon.log( 'tools.js' , 'getQs()' ,  "参数名称不能以数字开头，忽略了该参数" , name );
                        continue ;
                    }

                    var _argNameType = typeof arg[ name ] ;
                    switch ( _argNameType ){
                        case "undefined":
                            arg[ name ] = value ;
                            break;
                        case "string":
                            var a = [];
                            a.push( arg[name] );
                            a.push( value );
                            arg[ name ] = a ;
                            break ;
                        case "object":
                            if( Array.isArray( arg[ name ] ) ){
                                arg[ name ].push( value );
                            }else{
                                avalon.config.debug && avalon.log('tools.js' , 'getQs()' , "url参数" + name + "已经存在，但是存在错误");
                            }
                            break ;
                        default :
                            break;
                    }
                }
            }
            // avalon.log( arg )
            return arg ;
        };

        _obj.init();



    }

}(jQuery))