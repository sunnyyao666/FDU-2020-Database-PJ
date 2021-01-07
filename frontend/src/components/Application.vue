<template>
  <div id="base_application">
    <el-header style="height: 15%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Apply for a new meeting</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can make a application for a
            new meeting in this page.Note that <strong>all information is required</strong> here.</p>
        </el-col>
        <el-col :span="3" style="padding-top: 20px">
          <router-link to="personalInformation" style="text-decoration: none;" onmouseover="this.style.fontWeight='bold'"
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
    </el-header>

    <el-form :model="ApplicationForm"
             :rules="rules"
             class="application_container"
             label-position="left"
             label-width="0px"
             ref="ApplicationForm"
             v-loading="loading"
             style="height: 75%;margin-bottom: 30px">
      <h3 class="login_title">Create a meeting</h3>

      <el-form-item label=" " prop="abbreviation" style="margin: 20px;">
        <label class="date">Abbreviation:</label>

        <el-input type="text"
                  v-model="ApplicationForm.abbreviation"
                  auto-complete="off"
                  placeholder="abbreviation of title here"
                  class="input"
                  style="width: 350px"
        >
        </el-input>

      </el-form-item>
      <el-form-item label=" " prop="fullName" style="margin:20px;">
        <label class="date">Fullname:</label>
        <el-input type="text"
                  v-model="ApplicationForm.fullName"
                  auto-complete="off"
                  placeholder="full name of title here"
                  class="input"
                  style="width: 350px"
        >
        </el-input>
      </el-form-item>

      <el-form-item label=" " prop="place" style="margin:20px;">
        <label class="date">Place:</label>
        <el-input type="text"
                  v-model="ApplicationForm.place"
                  auto-complete="off"
                  placeholder="holding place here"
                  class="input"
                  style="width: 350px"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="date" style="margin:20px;" label=" ">
        <label class="date">Date:</label>
        <el-date-picker
          value-format="yyyy-MM-dd"
          v-model="ApplicationForm.date"
          :editable="false"
          type="daterange"
          range-separator="to"
          prefix-icon="el-icon-date"
          auto-complete="off"
          start-placeholder="start date here"
          end-placeholder="end date here"
          :picker-options="pickerOptions"
        ></el-date-picker>

      </el-form-item>
      <el-form-item prop="deadline" style="margin:20px;" label=" ">

        <label class="date">Deadline:</label>
        <el-date-picker style="width:350px"
                        value-format="yyyy-MM-dd"
                        v-model="ApplicationForm.deadline"
                        :editable="false"
                        type="date"
                        placeholder="deadline of submission here"
                        :picker-options="pickerOptions"
        >
        </el-date-picker>

      </el-form-item>
      <el-form-item prop="releaseTime" style="margin:20px;" label=" ">
        <label class="date">Release&nbsp;date:</label>
        <el-date-picker
          value-format="yyyy-MM-dd"
          style="width:350px"
          v-model="ApplicationForm.releaseTime"
          :editable="false"
          type="date"
          prefix-icon="el-icon-date"
          auto-complete="off"
          placeholder="release date of the result of assessment here"
          :picker-options="pickerOptions"
        >
        </el-date-picker>
      </el-form-item>
       <el-form-item label=" " prop="topic" style="margin:20px;">
        <label class="date" >Topic(s):</label>
        <el-button type="primary"
                   style="width:50px;background: dodgerblue;border-radius: 4px;height: 40px;"
                   v-on:click="addTopic">+
        </el-button>
        <el-button type="primary"
                   style="width:50px;background: dodgerblue;border-radius: 4px;height: 40px;"
                   v-on:click="deleteTopic">-
        </el-button>

        <!-- <el-input type="text"
                  v-for="(tp,i) of ApplicationForm.topic"
                  v-model="ApplicationForm.topic[i]"
                  :key="i"
                  auto-complete="off"
                  placeholder="topic here"
                  class="input"
                  style="width: 350px"
        >
        </el-input> -->


      </el-form-item>
      <el-form-item
                  v-for="(tp,i) of ApplicationForm.topic"
                  :key='i'
                  style="margin:20px;"
                  >
                  <label class="date" style="margin-left:12px;font-size:12px">   Topic{{i+1}}:</label>
                  <el-input v-model="ApplicationForm.topic[i]" placeholder="topic  here" style="width:350px"></el-input>
      </el-form-item>
      <el-form-item style="width: 100%;">
        <el-tooltip content="Note that empty topic will be ignored and eliminated automatically when you submit">
        <el-button type="primary"
                   style="width: 300px;background: dodgerblue;border-radius: 4px;height: 40px;"
                   v-on:click="submitForm('ApplicationForm')">submit
        </el-button>
        </el-tooltip>
      </el-form-item>

    </el-form>
    <el-footer style="text-align: center;height:5% ">Copyright © 2020 还没名字</el-footer>
  </div>

</template>

<script>
  export default {

    data() {
      const deadlineValid = (rule, value, callback) => {
          if (this.ApplicationForm.deadline>=this.ApplicationForm.releaseTime||this.ApplicationForm.deadline>=this.ApplicationForm.date[0]) {
            return callback(new Error('Invalid deadline!(Note that deadline should be earlier than release time and release time should be earlier than start date.)'))
          }
          return callback()
        };
      const dateValid=(rule,value,callback)=>{
        if(this.ApplicationForm.date[0]>=this.ApplicationForm.date[1]){
          return callback(new Error('Invalid date!(Note that the start date should be earlier than the end date.)'))
        }
        return callback()
      }
      const releaseTimeValid = (rule, value, callback) => {
        if (this.ApplicationForm.releaseTime>=this.ApplicationForm.date[0]) {
          return callback(new Error('Invalid release time!(Note that deadline should be earlier than release time and release time should be earlier than start date.)'))
        }
        return callback()
      };
      const topicValid=(rule,value,callback)=>{
        if(this.ApplicationForm.topic.length===0)
         return callback(new Error('should give at least one topic'))
       for(var i=0;i<this.ApplicationForm.topic.length;i++){
         if(this.ApplicationForm.topic[i]!==''){
            break;
         }

         if(i===this.ApplicationForm.topic.length-1)
          return callback(new Error('should give at least one topic'))

       }
        for(var i=0;i<this.ApplicationForm.topic.length;i++){
          for(var j=i+1;j<this.ApplicationForm.topic.length;j++){
            if(this.ApplicationForm.topic[i]!==''&&this.ApplicationForm.topic[i]===this.ApplicationForm.topic[j]){
              return callback(new Error('Invalid topic(Note that you have given the same topic twice or more)'))
            }
          }
        }
        return callback()
      };
      return {
        pickerOptions:{
          disabledDate(time){
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        ApplicationForm: {
          abbreviation: '',
          fullName: '',
          place: '',
          date: [],
          deadline: '',
          releaseTime: '',
          topic:['']
        },
        visible: false,
        rules: {
          abbreviation: [{required: true, message: '', trigger: 'blur'}],
          fullName: [{required: true, message: '', trigger: 'blur'}],
          place: [{required: true, message: '', trigger: 'blur'}],
          date: [{required: true, message: '', trigger: 'change'},{validator:dateValid,trigger: 'blur'}],
          deadline: [{required: true, message: '', trigger: 'change'},{validator: deadlineValid, trigger: 'blur'}],
          releaseTime: [{required: true, message: '', trigger: 'change'},{validator: releaseTimeValid, trigger: 'blur'}],
          topic:[{required: true, message: '', trigger: 'blur'},{validator:topicValid,trigger:'blur'}]
        },
        loading: false
      }
    },
    methods: {
      //提交表单函数
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
             for(var i=0;i<this.ApplicationForm.topic.length;i++){
               if(this.ApplicationForm.topic[i]===''){
                 this.ApplicationForm.topic.splice(i,1)
                 i--;
               }
             }
             let topicJson=JSON.stringify(this.ApplicationForm.topic)
             //console.log(topicJson);
            this.$axios.post('/apply', {
                abbreviation: this.ApplicationForm.abbreviation,
                fullName: this.ApplicationForm.fullName,
                place: this.ApplicationForm.place,
                startDate: this.ApplicationForm.date[0],
                endDate: this.ApplicationForm.date[1],
                deadline: this.ApplicationForm.deadline,
                releaseTime: this.ApplicationForm.releaseTime,
                topics: topicJson,
                token: this.$store.state.token
              }
            )
              .then(resp => {
                if (resp.status === 200) {
                  this.$message({
                    message: 'successful submit! \n Please wait for administrator\'s assessment. ',
                    type: 'success'
                  });
                  this.$router.replace({path: '/personalInformation'})
                } else {
                  this.$message({
                    message: 'The name of the conference has been used, please change the fullname of the conference you are applying!',
                    type: 'warning'
                  })
                }
              })
              .catch(error => {
                if (error.status === 403) {
                  this.$store.commit('logout');
                } else {
                  this.$message({
                    message: 'The name of the conference has been used, please change the fullname!',
                    type: 'warning',
                    showClose: true,
                    duration: 0
                  })
                }
              })
            // console.log(this.ApplicationForm.date)
            // console.log(this.ApplicationForm.topic)
          } else {

            return false;
          }
        });
      },
      addTopic(){
        this.ApplicationForm.topic.push("");
      },
      deleteTopic(){
        this.ApplicationForm.topic.pop("");
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },

      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      }
    }

  }


</script>

<style scoped>

  #base_application {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;

  }

  p {
    margin: 0;
  }

  .application_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .register_title {

    margin: 0 auto 10px auto;
    text-align: center;
    color: #505458;
  }

  .input {

    width: 300px;
    padding: 0;
  }

  .date {
    float: left;
    width: 0;
    padding-right: 12px;
    text-align: left;
  }

</style>
