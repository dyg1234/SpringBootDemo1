;!(function($){
    $.application = function(obj){
        var _obj = {
            $id:'list',
            system:"",
            theme:"",
            url:'/ixm/list.json',
            pagebar:{
                pageIndex:1,
                prepage:12
            },
            listArgs:{
                rsName:'rows'
            },
            loginArgs:{
                url:'',
                isLogin:false
            },
            searchArgs:{
                system:'',
                theme:''
            },
            pgTemp:''

        }


        avalon.mix(true, _obj, obj);

        var vmArgs = {
            $id:_obj.$id,
            isLogin:_obj.listArgs.isLogin,
            history:{},
            name:'',
            cache:false,
            result:[],
            list:[]

        }

        _obj.$vmList = avalon.define(vmArgs);

        var vmSearchArgs = {
            $id:_obj.$id + '_search',
            system:_obj.system,
            theme:_obj.theme,
            searchForm:_obj.searchArgs,
            name:'',
            $setVal:function(key, name , val , e ){
                //key值 name名称 val值
                e && e.preventDefault();
                _obj.$vmList.name = name;
                _obj.setVmSearchForm( key , val );
                _obj.tosearch();
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
                '&nbsp;&nbsp;<span>跳转到第&nbsp;<input ms-duplex="inputNumber" type="text" class="inp6">&nbsp;&nbsp;页&nbsp;&nbsp;<img src="/images/19ixm_gl_go.jpg"  ms-click="toPage(inputNumber,$event)"></span>',
            inputNumber:~~_obj.pagebar.pageIndex,
            toPage:function(index, e){
                (e) && ( e.preventDefault());
                _obj.toPage(index);
            }
        }

        _obj.$vmPage = avalon.define(_obj.vmPageArgs);

        _obj.getViewData = function(page, callback){

            if(_obj.$vmList.cache) {
                callback(_obj.dealViewData(_obj.$vmList.$model.history));
                return false;
            }
            var errorTip = _obj.errorTip ? _obj.errorTip:'没有符合条件的数据';
            _obj.$vmPage.pageIndex = page;
            $.ajax({
                url:_obj.url + "?randID=" + escape(new Date()),
                type:'get',
                dataType:'json',
                success:function( data ){
                    if(data && data.status){
                        if(typeof callback == 'function') {
                            callback(_obj.dealViewData(data));
                        }
                    }else{
                        if(typeof callback == 'function') {
                            callback(_obj.dealViewData({error:true, title:errorTip }));
                        }
                    }
                },error:function(e){
                    avalon.log(e.status);
                    if(typeof callback == 'function'){
                        callback(_obj.dealViewData({error:true, title:errorTip }));
                    }
                }
            })
        }



        _obj.dealViewData = function(data){
            var pageSize = _obj.$vmPage.prepage;
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
            var list = s[_obj.listArgs.rsName];
            var templist = [];
            var searchForm = _obj.$vmSearch.$model.searchForm;
            for(var i = 0; i < list.length; i++){
                if(searchForm.system && searchForm.theme){
                    if(list[i].sscope.indexOf(searchForm.system) > -1 && list[i].type == searchForm.theme){
                        templist.push(list[i]);
                    }
                }else if(searchForm.system){
                    if(list[i].sscope.indexOf(searchForm.system) > -1){
                        templist.push(list[i]);
                    }
                }else if(searchForm.theme){
                    if(list[i].type == searchForm.theme){
                        templist.push(list[i]);
                    }
                }else{
                    templist = list;
                    break;
                }
            }
            rs.recordCount = templist.length;
            rs.pageCount = Math.ceil(templist.length / pageSize);
            rs.list = templist;
            if(!_obj.$vmList.cache){
                _obj.$vmList.history = data;
                _obj.$vmSearch.theme = _obj.upCount(data.rows, _obj.theme)
                _obj.$vmSearch.system = _obj.upCount(data.rows, _obj.system, true);
                _obj.$vmList.cache = true;

            }
            if(templist.length == 0){
                return {error:true}
            }
            return rs;
        }



        //reg true情况下判断是不是systeam
        _obj.upCount = function(data, goal, reg){
            if(typeof data == 'undefined') return goal;
            for(var i = 0; i < data.length; i++){
                for(var n = 1; n < goal.length; n++){
                    if(!reg){
                        if(data[i].type == goal[n].value){
                            goal[n].count += 1;
                        }
                    }else{
                        if(data[i].sscope.indexOf(goal[n].value) > -1){
                            goal[n].count += 1;
                        }
                    }
                }
            }
            return goal;

        }

        _obj.setVmSearchForm = function( name , val ){      //  设置vm的searchForm属性，会根据searchArgs的定义，判断取值的有效性
            if(val == 'all' || val == ''){
                _obj.$vmSearch.searchForm[name] = '';
            }else if(val != ''){
                _obj.$vmSearch.searchForm[name] = val;
            }
        };


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

        _obj.tosearch = function(page){
            if(_obj.$vmList.cache && page > 0){
                console.log(1);
                _obj.$vmList.list = _obj.$vmList.$model.result[page - 1];
                _obj.$vmPage.pageIndex = page;
                _obj.$vmPage.inputNumber = page;
                return;
            }
            page = page ? page : 1;
            _obj.getViewData(page, function(msg){
                if(!msg.error){
                    var errorTip = _obj.errorTip ? _obj.errorTip:'没有符合条件的数据目录';
                    var list = msg.list;
                    var result = [];
                    var pageSize = _obj.$vmPage.prepage;
                    for(var i=0;i<list.length;i += pageSize){
                        result.push(list.slice(i,i+pageSize));
                    }
                    _obj.$vmList.result = result;
                    _obj.$vmList.list = result[_obj.$vmPage.pageIndex - 1];
                    _obj.$vmPage.recordCount = msg.recordCount;
                    _obj.$vmPage.pageCount = msg.pageCount;
                    _obj.$vmSearch.total = msg.total;
                    _obj.pageUtil(_obj.$vmPage.pageIndex, msg.pageCount);
                    if(msg.list.length == 0){
                        _obj.$vmList.list = [];
                        _obj.$vmPage.pageCount = 0;
                        _obj.$vmList.error = errorTip;
                    }else{
                        _obj.$vmList.error = false;
                    }
                }else{
                    _obj.$vmList.list = [];
                    _obj.$vmPage.pageCount = 0;
                    _obj.$vmList.error = msg.title
                }
            })
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

        _obj.toPage = function(index){
            index = ~~index;

            (index < 1) && (index = 1);

            if( _obj.$vmPage.pageCount > 0 ){
                (index >  _obj.$vmPage.pageCount )&&( index=_obj.$vmPage.pageCount );  //对 大于最大页码的参数，赋值为最大页码
            }

            _obj.$vmPage.pageIndex = index;


            _obj.tosearch(index);
        }

        _obj.init = function(){
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
                }
            }
            _obj.tosearch()
        }

        _obj.init();
    }
}(jQuery))