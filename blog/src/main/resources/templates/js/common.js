window.global = {};

global.baseUrl = './api/';
global.url = {
    listArticle: global.baseUrl + 'articles/list',
    listTag: global.baseUrl + 'tags/list',
    listArchive: global.baseUrl + 'archives/list',
    getBlogInfo: global.baseUrl + 'blog-info',
};

global.RESPONSE = {
    SUCCESS: 1
};

//发送ajax请求
global.ajax = function (options) {

    var ajaxDefaults = {
        type: 'GET',
        ContentType: 'application/x-www-form-urlencoded',
        dataType: 'json',
        error: function (xhr, status, error) {
            var msg = xhr.responseText;
            // console.error(xhr, status, error)
            // debugger;
        },
        complete: function (xhr, status) {
        },
        success: function (data, status, xhr) {
            if (data.code !== RESPONSE.SUCCESS) {
                console.error(data);
            }
        }
    };

    $.ajax($.extend(ajaxDefaults, options));
};