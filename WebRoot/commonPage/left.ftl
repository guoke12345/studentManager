    <div class="col-sm-2 col-lg-2">
            <div class="sidebar-nav">
                <div class="nav-canvas">
                    <div class="nav-sm nav nav-stacked">

                    </div>
                    <ul class="nav nav-pills nav-stacked main-menu">
                    
	                    <#if leftStatus == 0>
		                    <li class="nav-header">Menu</li>                     
	                        <li><a class="ajax-link" href="user/list"><i class=" glyphicon glyphicon-user"></i><span>  用户管理</span></a></li>
	                        <li><a class="ajax-link" href="stu/index"><i class="glyphicon glyphicon-align-justify"></i><span>  查看学生信息</span></a></li>
	                        <li><a class="ajax-link" href="info/index"><i class="glyphicon glyphicon-calendar"></i><span>  查看项目信息</span></a>
	                        </li>
	                        <li><a class="ajax-link" href="info/count"><i class="glyphicon glyphicon-signal"></i><span> 信息统计 </span></a>
	                        </li>
	                        <li><a class="ajax-link" href="stu/toAddClass"><i class="glyphicon glyphicon-plus"></i><span>  专业班级管理</span></a>
	                        </li>
	                        <li><a class="ajax-link" href="pro/index"><i class="glyphicon glyphicon-wrench"></i><span>  项目管理</span></a>
	                        </li>
	                        <li><a class="ajax-link" href="upload/toStuTable"><i class="glyphicon glyphicon-random"></i><span>  导入学生信息表</span></a>
	                        </li>
	                        <li><a class="ajax-link" href="upload/toDetailTable"><i class="glyphicon glyphicon-random"></i><span>  导入学生学期资料</span></a>
                        	</li>
	                        <li><a class="ajax-link" href="upload/toProTable"><i class="glyphicon glyphicon-share"></i><span>  导入项目信息表</span></a>
	                        </li>
	                       <!--  <li><a class="ajax-link" href="stu/toSetData"><i class=" glyphicon glyphicon-list-alt"></i><span>  设置校历时间   </span></a>
	                        </li> -->
	                    <#else>
	                        <li><a class="ajax-link" href="info/index"><i class="glyphicon glyphicon-calendar"></i><span>  查看项目信息</span></a>
	                        </li>
	                        <li><a class="ajax-link" href="info/count"><i class="glyphicon glyphicon-signal"></i><span> 信息统计 </span></a>
	                        </li>                 
	                    </#if>
                    </ul>
                    <!--<label id="for-is-ajax" for="is-ajax"><input id="is-ajax" type="checkbox"> Ajax on menu</label>-->
                </div>
            </div>
        </div>