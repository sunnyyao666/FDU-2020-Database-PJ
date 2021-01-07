<template>
  <div id="base_discussion" class="top">
          <el-header style="height: 25%;padding-left: 5em;" id='target' >
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Discussion for audit</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">The post here is for chair and PCs to discuss on theses.</p>
        </el-col>
        <el-col :span="3" style="padding-top: 20px">
          <router-link to="personalInformation" style="text-decoration: none;"
                       onmouseover="this.style.fontWeight='bold'"
                       onmouseout="this.style.fontWeight='normal'">
            <el-tooltip content="Go to personal information page">
              <p>
                {{this.$store.state.username}}
              </p>
            </el-tooltip>
          </router-link>
          <el-popover placement="bottom" v-model="visible">
            <p>Are you sure you want to log out?</p>
            <el-row style="text-align: right;margin-top:7px;">
              <el-button size="mini" @click="visible=false">No</el-button>
              <el-button type="primary" size="mini" @click="logout">Yes</el-button>
            </el-row>
            <el-button plain style="margin-top: 10px;" slot="reference">
              Log out
            </el-button>
          </el-popover>
        </el-col>
      </el-row>
      <el-row>
       
      <el-button type='primary' style='position:absolute;left:50px' @click="backTOPC">Back to PC</el-button>
        
      <h1 style='margin-top:0px'>
       Title: {{title}}
      </h1>
      </el-row>
    </el-header>
    <el-backtop target='.top'></el-backtop>
    <div class='discussion_container'>
    <el-table :data='discuss.slice((currentPage-1)*pageSize,currentPage*pageSize)' >
        <el-table-column width='120'>
            <template slot-scope='props'>
                <div class='user'>
                    <em class="el-icon-user"></em>
                  <span>  {{props.row.username}}</span>

                </div>
            </template>
        </el-table-column>
        <el-table-column >
               <template slot-scope='props'>
                <div class='content'>
                  <p>  {{props.row.text}}</p>
                  <div class='floor'>
                       <span>{{(currentPage-1)*pageSize+props.$index+1}} floor</span>
                  </div>
                  <div class='time'>
                       <span>{{props.row.createTime}}</span>
                  </div>

                </div>
            </template>
        </el-table-column>

    </el-table>
    <div id="sum" >
       <!-- <span style="color:RED">{{discuss.length}}</span> posts
       <span style="color:RED; margin-left:10px">{{discuss.length%10==0?discuss.length/10:parseInt(discuss.length/10)+1}}</span> pages -->
 <el-pagination  @current-change="handleCurrentChange"
                           :current-page="currentPage"  :page-size="pageSize"
                           layout="total,prev,pager,next,jumper" :total="discuss.length"></el-pagination>
    </div>
    </div>
    <el-form :model='newPost' class='discussion_container' id='post'>
         <el-form-item label="new post">
    <el-input type="textarea" v-model="newPost.text"></el-input>
  </el-form-item>
  <el-form-item>
      <el-button type='primary' style='float:right; margin-right:150px' @click="post">
          post
      </el-button>
  </el-form-item>
    </el-form>
     <el-footer style="text-align: center;height:10%;padding-top: 20px">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
let timer=null
export default {
    // props:{
    //   step:{   //此数据是控制动画快慢的
    //     type:Number,
    //     default:100
    //   }
    // },
    data(){
        return{
                discuss:[],
        visible:false,
        scrollTop: null, //初始化scrollTop
        visiable: false, //默认不显示
        newPost:{
            text:''
        },
        pageSize:10,
        currentPage: 1,
        title:''
        }

    },
    methods:{
      backTOPC(){
        this.$router.replace('/pcmember');
      },
      convertUTCTimeToLocalTime(UTCDateString) {
        if(!UTCDateString){
          return '-';
        }
        function formatFunc(str) {    //格式化显示
          return str > 9 ? str : '0' + str
        }
        var date2 = new Date(UTCDateString);     //这步是关键
        var year = date2.getFullYear();
        var mon = formatFunc(date2.getMonth() + 1);
        var day = formatFunc(date2.getDate());
        var hour = date2.getHours();
        var noon = hour >= 12 ? 'PM' : 'AM';
        hour = hour>12?hour-12:hour;
        hour = formatFunc(hour);
        var min = formatFunc(date2.getMinutes());
        var dateStr = year+'-'+mon+'-'+day+' '+noon +' '+hour+':'+min;
        return dateStr;
},


      update(){
        this.$axios.post('/getThesis',{
            thesisID:this.$store.state.thesisID
          })
          .then(resp=>{
           this.discuss=resp.data.posts;
           this.title=resp.data.title;
           this.discuss.sort(this.sortTime);
           this.newPost.text='';
           console.log(this.discuss[0].createTime)
           for(var i=0;i<this.discuss.length;i++){
             this.discuss[i].createTime=this.convertUTCTimeToLocalTime(this.discuss[i].createTime);
           }
           console.log(this.discuss)
           console.log(this.convertUTCTimeToLocalTime("2020-06-14T05:55:15.397+0000"))
          })
            .catch(error => {
          console.log(error)

        })
      },
      sortTime(a,b){
        return Date.parse(a.createTime)-Date.parse(b.createTime)
      },
        post(){
         // console.log(this.newPost.text)
         if(this.newPost.text===''){
            this.$message({
                    message: 'Can not post nothing',
                    type: 'warning'
                  })
                 return;
         }
            this.$axios.post('/post',{
              thesisID:this.$store.state.thesisID,
              text:this.newPost.text
            })
            .then(resp=>{

              if(resp.status==200){
                target.scrollIntoView();
                 this.$message({
                    message: 'success',
                    type: 'success'
                  })
                this.update();

              }
            })
                 .catch(error => {
          console.log(error)

        })
        },
            logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      },

      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage;
        //this.backtop();
         target.scrollIntoView();

      },


    },
    beforeMount(){
          this.update();
    }



}
</script>

<style>
#base_discussion{
    overflow: auto;
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;

}
.user{
    min-height:100px;
    text-align: center;
    line-height:100px;


}
.content{
    min-height:100px;
    padding-left:20px;
    position:relative;
}
.time{
    font-size: 12px;
    position: absolute;
    left:594px;
    top:82px;
    text-align: right;
    color: #999;

}
.floor{
   font-size: 12px;
    position: absolute;
    left:500px;
    top:82px;
    text-align: right;
    color: #999;

}
.el-table_1_column_1  {
background-color:#fbfbfd;
}
#sum div.el-pagination{
/*padding-left:50px;*/
background-color:#f7f8fa;
text-align:center;
border-bottom: 1px solid #E5E5E5;
border-top: 1px solid #E5E5E5;
line-height: 60px;
height:60px;
font-size: 12px;
font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
color: #333;
}

 .discussion_container {
    /* border-radius: 15px; */
   background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 850px;
    padding: 15px -35px 15px -35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  #post{
      background: #f7f8fa;
  }
  #post label{
     margin-left: 80px;
     font-size:16px;
     font-weight:bolder;

  }
  #post textarea{
      width:600px;
      height:200px;
      margin:0 auto
  }


</style>
