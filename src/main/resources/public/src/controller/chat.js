layui.define(['table', 'form'], function (exports) {
    var $ = layui.$
        , layer = layui.layer
        , laytpl = layui.laytpl
        , setter = layui.setter
        , view = layui.view
        , admin = layui.admin
        , table = layui.table
        , form = layui.form;
    //----------时间戳的处理开始----------
    layui.laytpl.toDateString = function (d, format) {
        var date = new Date(d || new Date())
            , ymd = [
            this.digit(date.getFullYear(), 4)
            , this.digit(date.getMonth() + 1)
            , this.digit(date.getDate())
        ]
            , hms = [
            this.digit(date.getHours())
            , this.digit(date.getMinutes())
            , this.digit(date.getSeconds())
        ];
        format = format || 'yyyy-MM-dd ';
        return format.replace(/yyyy/g, ymd[0])
            .replace(/MM/g, ymd[1])
            .replace(/dd/g, ymd[2])
            .replace(/HH/g, hms[0])
            .replace(/mm/g, hms[1])
            .replace(/ss/g, hms[2]);
    };
    //数字前置补零
    layui.laytpl.digit = function (num, length, end) {
        var str = '';
        num = String(num);
        length = length || 2;
        for (var i = num.length; i < length; i++) {
            str += '0';
        }
        return num < Math.pow(10, length) ? str + (num | 0) : num;
    };
    //----------时间戳的处理结束----------

    //**********表格显示开始**********
    table.render({
        elem: '#LAY-chat-list'
        , url: '/words/s/chat/query'
        , cols: [[
            {field: 'chat_id',  title: 'ID',templet: '<div>{{d.LAY_TABLE_INDEX+1}}</div>'}
            , {field: 'chat_user_id', title: '聊天用户',templet: function(d){
                    if(d.user!=null){
                        return d.user.user_nickname;
                    }
                    return "";
                }}
            , {field: 'chat_content', title: '聊天内容'}
            , {field: 'chat_pic', title: '聊天图片',templet:"#Img"}
            , {field: 'create_time', title: '创建日期'}
            , {title: '操作', width: 160, align: 'center', fixed: 'right', toolbar: '#table-chat-toolbar'}//设置表格工具条的名称
        ]]
        , page: true//开启分页
        , limit: 20
        , limits: [20, 25, 30, 35, 40]
        , text: '对不起，加载出现异常！'
    });
    //**********表格显示开始***********

    //++++++++++监听工具条操作开始++++++++++
    table.on('tool(LAY-chat-list)', function (obj) {//表格的名称
        var data = obj.data;
        if (obj.event === 'edit') {//匹配工具栏的edit字段
            admin.popup({
                title: '修改词条信息'
                , area: ['550px', '550px']
                , success: function (layero, index) {
                    view(this.id).render('chat/form', data).done(function () {//跳转的路径
                        form.render(null, 'LAY-chat-list');//读取表格的信息
                        //监听提交
                        form.on('submit(chat-form-submit)', function (data) {//form 表单提交的按钮
                            var field = data.field; //获取提交的字段
                            console.log(field)
                            $.ajax({
                                type: "POST", //请求方式 post
                                dataType: 'json', //数据类型 json
                                contentType: "application/json; charset=utf-8",
                                url: "/words/s/chat/update", // 请求地址
                                data: JSON.stringify(field), //请求附带参数
                                success: function () {
                                    layui.table.reload('LAY-additional-additional'); //重载表格
                                    layer.close(index); //执行关闭
                                }
                            });
                        });
                    });
                }
            });
        } else if (obj.event === 'del') {//匹配工具栏的del字段
            layer.confirm('确定删除词条信息？', function (index) {
                var id = data.chat_id;//根据数据库的字段更改data.id中id的命名
                $.post("/words/s/chat/delete", {id: id}, function (data) {
                    obj.del();
                    layer.close(index);
                })
            });
        }
    });
    //++++++++++监听工具条操作开始++++++++++

    //**********新增开始**********
    var active = {
        add: function () {
            admin.popup({
                title: '添加词条'
                , area: ['550px', '550px']//设置弹出框大小
                , success: function (layero, index) {
                    view(this.id).render('chat/form').done(function () {
                        //监听提交
                        form.on('submit(chat-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            console.log(field)
                            $.ajax({
                                type: "POST", //请求方式 post
                                dataType: 'json', //数据类型 json
                                contentType: "application/json; charset=utf-8",
                                url: "/words/s/chat/add", // 请求地址
                                data: JSON.stringify(field), //请求附带参数
                                success: function (data) {
                                    layui.table.reload('LAY-chat-list'); //重载表格
                                    layer.close(index); //执行关闭
                                }
                            });
                        });
                    });
                }
            });
        }
    }
    $('.layui-btn.chat-form').on('click', function() {var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    //**********新增结束**********

    //==========搜索开始==========
    form.render(null, 'lay-admin-chat-form');
    form.on('submit(LAY-chat-back-search)',
        function(data) {
            var field = data.field;
            console.log(field)
            //执行重载
            table.reload('LAY-chat-list', {
                method: "post",
                url: "/words/s/chat/search",
                where: field
            });
        });
    //==========搜索结束==========

    //对外暴露的接口
    exports('chat', {});
});

// $(function() {
//     var FADE_TIME = 150; // ms
//     var COLORS = [
//         '#e21400', '#91580f', '#f8a700', '#f78b00',
//         '#58dc00', '#287b00', '#a8f07a', '#4ae8c4',
//         '#3b88eb', '#3824aa', '#a700ff', '#d300e7'
//     ];
//
//     // Initialize variables
//     var $window = $(window);
//     var $usernameInput = $('.usernameInput'); // 昵称
//     var $messages = $('.messages'); // 消息区域
//     var $inputMessage = $('.inputMessage'); // 消息框
//     var $loginPage = $('.login.page'); // 登录页
//     var $chatPage = $('.chat.page'); // 聊天室页
//
//     // WebSocket
//     var ws = new WebSocket("ws://172.16.1.233:9090");
//
//     // Prompt for setting a username
//     var username;
//     var connected = false; // 连接状态
//     var $currentInput = $usernameInput.focus();
//
//     // 设置昵称
//     function setUsername () {
//         username = cleanInput($usernameInput.val().trim());
//         if (username) {
//             $loginPage.fadeOut();
//             $chatPage.show();
//             $loginPage.off('click');
//             $currentInput = $inputMessage.focus();
//             // 发一个进入房间的消息给服务器
//             var msg = {};
//             msg.t = 1;
//             msg.n = username;
//             // 通常情况下，房间标识在服务端处理，想测试可以直接使用 url 中的参数输入或者在页面上参数输入
//             msg.room_id = 1;
//             ws.send(JSON.stringify(msg));
//         }
//     }
//
//     // 输出日志信息
//     function log (message, options) {
//         var $el = $('<li>').addClass('log').text(message);
//         addMessageElement($el, options);
//     }
//
//     // 输出聊天信息
//     function addChatMessage (data, options) {
//         options = options || {};
//         var $usernameDiv = $('<span class="username"/>')
//             .text(data.username)
//             .css('color', getUsernameColor(data.username));
//         var $messageBodyDiv = $('<span class="messageBody">')
//             .text(data.message);
//
//         var typingClass = data.typing ? 'typing' : '';
//         var $messageDiv = $('<li class="message"/>')
//             .data('username', data.username)
//             .addClass(typingClass)
//             .append($usernameDiv, $messageBodyDiv);
//
//         addMessageElement($messageDiv, options);
//     }
//
//     // DOM 操作
//     function addMessageElement (el, options) {
//         var $el = $(el);
//
//         if (!options) {
//             options = {};
//         }
//         if (typeof options.fade === 'undefined') {
//             options.fade = true;
//         }
//         if (typeof options.prepend === 'undefined') {
//             options.prepend = false;
//         }
//
//         if (options.fade) {
//             $el.hide().fadeIn(FADE_TIME);
//         }
//         if (options.prepend) {
//             $messages.prepend($el);
//         } else {
//             $messages.append($el);
//         }
//         $messages[0].scrollTop = $messages[0].scrollHeight;
//     }
//
//     // 清除输入框中注入的信息
//     function cleanInput (input) {
//         return $('<div/>').text(input).html();
//     }
//
//     // 通过 hash 函数给用户名上色
//     function getUsernameColor (username) {
//
//         var hash = 7;
//         for (var i = 0; i < username.length; i++) {
//             hash = username.charCodeAt(i) + (hash << 5) - hash;
//         }
//         // 计算颜色下标
//         var index = Math.abs(hash % COLORS.length);
//         return COLORS[index];
//     }
//
//     // Keyboard events
//
//     $window.keydown(function (event) {
//         // 回车后依旧获取焦点
//         if (!(event.ctrlKey || event.metaKey || event.altKey)) {
//             $currentInput.focus();
//         }
//         // 监听回车键
//         if (event.which === 13) {
//             if (username) {
//                 sendMessage();
//
//             } else {
//                 setUsername();
//             }
//         }
//     });
//
//     // 获取焦点
//     $loginPage.click(function () {
//         $currentInput.focus();
//     });
//
//     // 建立连接的时候更新连接状态
//     ws.onopen = function (e) {
//         console.log('Connection to server opened');
//         connected = true;
//     }
//
//     // 处理服务器发送过来的消息
//     ws.onmessage = function(e) {
//         var msg = JSON.parse(e.data);
//         console.log(msg);
//         switch(msg.t) {
//             case 0:
//                 // 建立连接的响应
//                 break;
//             case -1:
//                 // 收到进入房间的响应 包含房间信息
//                 log("欢迎 " + username + " 进入聊天室");
//                 break;
//             case -2:
//                 // 收到其他人发过来的消息
//                 var data = {
//                     username: msg.n,
//                     message: msg.body
//                 };
//                 addChatMessage(data);
//                 break;
//             case -10001:
//                 // 收到其他人进入房间的消息
//                 log(msg.n + " 进入了聊天室");
//                 break;
//             case -11000:
//                 // 收到其他人离开房间的信息
//                 log("用户 " + msg.n + " 离开了聊天室")
//                 break;
//         }
//     }
//
//     ws.onclose = function(e) {
//         // 可以在 onclose 和 onerror 中处理重连的逻辑，再决定是否将状态更新为未连接状态
//         connected = false;
//     }
//
//     ws.onerror = function(e) {
//         connected = false;
//     }
//     // 发送消息
//     function sendMessage() {
//         if(connected) {
//             var msg = {};
//             msg.t = 2;
//             msg.n = username;
//             msg.body = cleanInput($inputMessage.val());
//             ws.send(JSON.stringify(msg));
//             addChatMessage({username:username,message:msg.body});
//             $inputMessage.val("");
//         }else {
//             log("与服务器断开连接了，刷新重新连接~");
//         }
//
//     }
//
// });