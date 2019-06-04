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
        elem: '#LAY-report-list'
        , url: '/words/s/report/query'
        , cols: [[
              {field: 'report_id',  title: 'ID',templet: '<div>{{d.LAY_TABLE_INDEX+1}}</div>'}
            , {field: 'type', title: '举报类型', templet:function (d) {
                    if(d.type!=null){
                        if(d.type==1){
                            return "话题举报";
                        }else{
                            return '评论举报';
                        }
                    }else {
                        return '';
                    }
                } }
            , {field: 'report_user_id', title: '举报用户', templet:function(d){
                    if(d.user!=null){
                      return d.user.user_nickname
                    }
                    return "";
                }}
            , {field: 'report_content', title: '举报内容'}
            , {field: 'create_time', title: '创建日期'}
            , {title: '操作', width: 160, align: 'center', fixed: 'right', toolbar: '#table-report-toolbar'}//设置表格工具条的名称
        ]]
        , page: true//开启分页
        , limit: 20
        , limits: [20, 25, 30, 35, 40]
        , text: '对不起，加载出现异常！'
    });
    //**********表格显示开始***********

    //++++++++++监听工具条操作开始++++++++++
    table.on('tool(LAY-report-list)', function (obj) {//表格的名称
        var data = obj.data;
        if (obj.event === 'edit') {//匹配工具栏的edit字段
            admin.popup({
                title: '修改词条信息'
                , area: ['550px', '550px']
                , success: function (layero, index) {
                    view(this.id).render('report/form', data).done(function () {//跳转的路径
                        form.render(null, 'LAY-report-list');//读取表格的信息
                        //监听提交
                        form.on('submit(report-form-submit)', function (data) {//form 表单提交的按钮
                            var field = data.field; //获取提交的字段
                            console.log(field)
                            $.ajax({
                                type: "POST", //请求方式 post
                                dataType: 'json', //数据类型 json
                                contentType: "application/json; charset=utf-8",
                                url: "/words/s/report/update", // 请求地址
                                data: JSON.stringify(field), //请求附带参数
                                success: function () {
                                    layui.table.reload('LAY-report-list'); //重载表格
                                    layer.close(index); //执行关闭
                                }
                            });
                        });
                    });
                }
            });
        } else if (obj.event === 'del') {//匹配工具栏的del字段
            layer.confirm('确定删除举报条目？', function (index) {
                layer.close(index);
                var id = data.report_id;//根据数据库的字段更改data.id中id的命名
                $.post("/words/s/report/ignore", {id: id}, function (data) {
                    if(data.meta.code==200){
                        top.layer.msg("操作成功")
                        obj.del();
                    }
                })
            });
        }else if(obj.event === 'ignore'){
            layer.confirm('确定忽略举报信息？', function (index) {
                layer.close(index);
                var id = data.report_id;
                $.post("/words/s/report/delete", {id: id}, function (data) {
                    if(data.meta.code==200){
                        top.layer.msg("操作成功")
                        obj.del();
                    }
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
                    view(this.id).render('report/form').done(function () {
                        //监听提交
                        form.on('submit(report-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            console.log(field)
                            $.ajax({
                                type: "POST", //请求方式 post
                                dataType: 'json', //数据类型 json
                                contentType: "application/json; charset=utf-8",
                                url: "/words/s/report/add", // 请求地址
                                data: JSON.stringify(field), //请求附带参数
                                success: function (data) {
                                    layui.table.reload('LAY-report-list'); //重载表格
                                    layer.close(index); //执行关闭
                                }
                            });
                        });
                    });
                }
            });
        }
    }
    $('.layui-btn.report-form').on('click', function() {var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    //**********新增结束**********

    //==========搜索开始==========
    form.render(null, 'lay-admin-report-form');
    form.on('submit(LAY-report-back-search)',
        function(data) {
            var field = data.field;
            console.log(field)
            //执行重载
            table.reload('LAY-report-list', {
                method: "post",
                url: "/words/s/report/search",
                where: field
            });
        });
    //==========搜索结束==========

    //对外暴露的接口
    exports('report', {});
});