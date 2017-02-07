 <div class="navbar navbar-default" role="navigation">

        <div class="navbar-inner">
            <button type="button" class="navbar-toggle pull-left animated flip">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="width:390px;" href="javascript:;"> <img alt="Charisma Logo" src="img/logo20.png" class="hidden-xs"/>
                <span>国际学院学生数据管理</span></a>

            <!-- user dropdown starts -->
           
           	<div class="btn-group pull-right">
                <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <i class="glyphicon glyphicon-user"></i><span class="hidden-sm hidden-xs"> ${sessionUserName!}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <li><a href="user/toUpdatePass">修改密码</a></li>
                    <li class="divider"></li>
                    <li><a href="user/exit">退出</a></li>
                </ul>
            </div>
            
        </div>
    </div>
