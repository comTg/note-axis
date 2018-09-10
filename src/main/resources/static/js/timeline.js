// 弹出提示
function popupToast(heading,text,icon){
    $.toast({
        heading: heading,
        text: text,
        showHideTransition: 'slide',
        icon: icon,
        position: 'bottom-right',
    });
}

function register(){
    $("#btnRegister").on("click",function(event){
        event.preventDefault();
        console.log("阻止提交表单");
        const registerUrl = "/user/register";
        let form_values = $("#registerForm").serializeArray();
        let datas = {};
        for (let x in form_values) {
            datas[form_values[x].name] = form_values[x].value;
        }
        if(datas.repassword != datas.password){
            popupToast("Error","两次密码不一致,请重新输入!","error");
            return;
        }
        delete datas.repassword;
        $.ajax({
           url: registerUrl,
           data: datas,
           type: 'post',
           success: function(res){
                if(res.rspCode=='000000'){
                    popupToast("Success","注册成功!","success");
                }else{
                    popupToast("Error",res.rspMsg,"error");
                }
           }
        });
    });
}

function login(){
    $("#btnLogin").on("click",function(event){
       event.preventDefault();
       const loginUrl = "/user/login";
       let form_values = $("#loginForm").serializeArray();
       let datas = {};
       for(let x in form_values){
           datas[form_values[x].name] = form_values[x].value;
       }
       $.ajax({
          url: loginUrl,
          data: datas,
          type: 'post',
          success: function(res){
              if(res.rspCode=='000000'){
                  window.location.href = '/';
                  popupToast("Success","登录成功!","success");
              }else{
                  popupToast("Error",res.rspMsg,"error");
              }
          }
       });
    });
}

function addAreaSwitch(){
    $("#btn-add").on("click",function(){
        if(is_addarea_show){
            $("#add-area").hide();
            $(this).css("color","red");
        }else{
            $("#add-area").show();
            $(this).css("color","rgba(0,0,0,.5)");
        }
        is_addarea_show = !is_addarea_show;
    });
}

function addContent(){
    $("#btnAdd").on("click",function(event){
        event.preventDefault();
        const addUrl = "/api/timeline/add";
        let form_values = $("#addForm").serializeArray();
        let datas = {};
        for(let x in form_values){
            datas[form_values[x].name] = form_values[x].value;
        }
        console.log(datas);
        $.ajax({
           url: addUrl,
           data: datas,
           type: 'post',
           success: function(res){
               if(res.rspCode=='000000'){
                   popupToast("Success","add success!","success");
                   loadTimeLine();
                   $("#addContent").val("");

               }else{
                   popupToast("Error",res.rspMsg,"error");
               }
           }
        });
    });
}

function appendData(items,isAppend=true){
    let main = $("#main-timeline");
    if(!isAppend){
        main.html("");
    }
    let block,item,content,month,time;
    for(var i = 0;i<items.length;i++){
        block = "";
        item = items[i];
        month = dayjs(item.createTime).format("M月DD");
        time = dayjs(item.createTime).format("HH:mm");
        if(item.isMd=="1"){
            content = marked(item.content);
        }else{
            content = item.content;
        }
        block += '<li class="t-timeline-item"><i class="t-timeline-axis"></i>';
        block += '<div class="t-timeline-content t-text"><h3 class="t-timeline-title">'+month+'<span class="t-timeline-time">'+time+'</span></h3>';
        block += content + '</div></li>';
        main.append(block);
    }
}

function loadTimeLine(page=0,isAppend=false){
    const loadUrl = "/api/timeline/load";
    console.log("load timeline start:");
    datas = {"page":page};
    $.ajax({
       url: loadUrl,
        data: datas,
        type: 'post',
        success: function(res){
           console.log(res);
           if(typeof(res)=="string"){
               $("#main-timeline").html("<h3>请先登录后查看</h3>");
           } else if(res.rspCode='000000'){
               max_page = res.data.totalPages;
               appendData(res.data.content,isAppend);
           }
        }
    });
}

function search(){
    var key = $("#searchInput").val();
    if(key!=null && key.trim()!=""){
        const searchUrl = "/api/timeline/query";
        $.ajax({
            url: searchUrl,
            data: {"key":key},
            success: function(res){
                if(res.rspCode=='000000'){
                    appendData(res.data,false);
                }
            }
        });
    }
    return false;
}

//判断是否已经到底部了
function toBottom() {
    var scrollTop = $(window).scrollTop();
    var clientHeight = $(window).height();
    var scrollHeight = $(document).height();
    if (scrollTop + clientHeight == scrollHeight ) {
        return true;
    } else {
        return false;
    }
}

function steamLoad(){
    $(window).on("scroll",function(){
        if(toBottom()){
            if(cur_page<max_page-1){
                cur_page += 1;
                loadTimeLine(cur_page,true);
            }else{
                popupToast('Tips',"当前已经到最后一页了!",'info');
            }
        }
    });
}

