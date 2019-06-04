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
        elem: '#LAY-eqitem-list'
        , url: '/words/s/eqitem/query'
        , cols: [[
             {field: 'eqitem_id',  title: 'ID',templet: '<div>{{d.LAY_TABLE_INDEX+1}}</div>'}
            , {field: 'eqitem_test_id', title: '测试试卷',templet: '<div>{{d.eqtest.eqtest_title}}</div>'}
            , {field: 'eqitem_content_id', title: '题目内容',templet: '<div>{{d.eqcontent.eqcontent_content}}</div>'}
            , {field: 'eqitem_content', title: '选项内容'}
            , {field: 'mark', title: '解读问题'}
            , {field: 'eqitem_point', title: '选项分数'}
            , {field: 'eqitem_select', title: '选项目标'}
            , {field: 'create_time', title: '创建日期'}
            , {title: '操作', width: 160, align: 'center', fixed: 'right', toolbar: '#table-eqitem-toolbar'}//设置表格工具条的名称
        ]]
        , page: true//开启分页
        , limit: 20
        , limits: [20, 25, 30, 35, 40]
        , text: '对不起，加载出现异常！'
    });
    //**********表格显示开始***********

    //++++++++++监听工具条操作开始++++++++++
    table.on('tool(LAY-eqitem-list)', function (obj) {//表格的名称
        var data = obj.data;
        if (obj.event === 'edit') {//匹配工具栏的edit字段
            admin.popup({
                title: '修改词条信息'
                , area: [ 50 + '%', 70 + '%' ]
                , success: function (layero, index) {
                    view(this.id).render('eqitem/form', data).done(function () {//跳转的路径
                        eqcontentid=data.eqitem_content_id;
                        eqtestid=data.eqitem_test_id;
                        form.render(null, 'eqitem-form');//读取表格的信息

                        $('#eqitem_content_id').html('')//清空下拉框防止option标签重叠
                        $.get("/words/s/eqcontent/query/" + eqcontentid, {},
                            function (data) {
                                var ssss=data.data;
                                var $html = "";
                                if (data != null) {
                                    $.each(ssss,
                                        function (index, item) {
                                            if(item.eqcontent_id==eqcontentid){
                                                $html += "<option value='" + item.eqcontent_id + "' selected='selected'>" + item.eqcontent_content + "</option>";
                                            }else {
                                                $html += "<option value='" + item.eqcontent_id + "'>" + item.eqcontent_content + "</option>";
                                            }
                                        });
                                    $('#eqitem_content_id').append($html);
                                    form.render('select');
                                }
                            });

                        //监听提交
                        form.on('submit(eqitem-form-submit)', function (data) {//form 表单提交的按钮
                            var field = data.field; //获取提交的字段
                            console.log(field)
                            $.ajax({
                                type: "POST", //请求方式 post
                                dataType: 'json', //数据类型 json
                                contentType: "application/json; charset=utf-8",
                                url: "/words/s/eqitem/update", // 请求地址
                                data: JSON.stringify(field), //请求附带参数
                                success: function () {
                                    layui.table.reload('LAY-eqitem-list'); //重载表格
                                    layer.close(index); //执行关闭
                                }
                            });
                        });
                    });
                }
            });
        } else if (obj.event === 'del') {//匹配工具栏的del字段
            layer.confirm('确定删除词条信息？', function (index) {
                var id = data.eqitem_id;//根据数据库的字段更改data.id中id的命名
                $.post("/words/s/eqitem/delete", {id: id}, function (data) {
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
                , area: [ 50 + '%', 70 + '%' ]//设置弹出框大小
                , success: function (layero, index) {
                    view(this.id).render('eqitem/form').done(function () {
                        //监听提交
                        form.on('submit(eqitem-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            console.log(field)
                            $.ajax({
                                type: "POST", //请求方式 post
                                dataType: 'json', //数据类型 json
                                contentType: "application/json; charset=utf-8",
                                url: "/words/s/eqitem/add", // 请求地址
                                data: JSON.stringify(field), //请求附带参数
                                success: function (data) {
                                    layui.table.reload('LAY-eqitem-list'); //重载表格
                                    layer.close(index); //执行关闭
                                }
                            });
                        });
                    });
                }
            });
        }
    }
    $('.layui-btn.eqitem-form').on('click', function() {var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    //**********新增结束**********

    //==========搜索开始==========
    form.render(null, 'lay-admin-eqitem-form');
    form.on('submit(LAY-eqitem-back-search)',
        function(data) {
            var field = data.field;
            console.log(field)
            //执行重载
            table.reload('LAY-eqitem-list', {
                method: "post",
                url: "/words/s/eqitem/search",
                where: field
            });
        });
    //==========搜索结束==========

    //对外暴露的接口
    exports('eqitem', {});
});