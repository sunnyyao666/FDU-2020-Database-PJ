<template>
  <div id="base_contribution" style="overflow: auto">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Contribution</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can make your contribution
            here. Note that <strong>the title should be no more than 50 letters</strong> while <strong>the summary shouldn't be more
              than 800 letters</strong>, and we <strong>only support a single PDF file</strong> as your thesis submission.</p>
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
    </el-header>
    <el-main style="min-height: 70%">
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item name="1">
          <template slot="title">
            <h3>Information&nbsp;</h3><em class="header-icon el-icon-info"></em>
          </template>
          <el-form class="information_container" style="margin: 20px auto">
            <h3>Conference Information</h3>
            <el-table :data="informationData">
              <el-table-column
                prop="conferenceFullName"
                label="Fullname"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="abbreviation"
                label="Abbreviation"
                align="center"
              >
              </el-table-column>
              <el-table-column
                prop="place"
                label="Place"
                align="center"
              >
              </el-table-column>
            </el-table>
            <el-table :data="informationData">
              <el-table-column
                prop="startDate"
                label="Start Date"
                align="center"
              >
              </el-table-column>
              <el-table-column
                prop="endDate"
                label="End Date"
                align="center"
              >
              </el-table-column>
              <el-table-column
                prop="deadline"
                label="Deadline"
                align="center"
              >
              </el-table-column>
              <el-table-column
                prop="releaseTime"
                label="Release Time"
                align="center"
              >
              </el-table-column>
            </el-table>
          </el-form>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <h3>Contribution&nbsp;</h3><em class="header-icon el-icon-upload"></em>
          </template>
          <el-form :model="contributionForm" :rules="rules" label-position="top"
                   label-width="0px"
                   v-loading="loading"
                   :ref="contributionForm"
                   enctype="multipart/form-data"
                   class="contribution_container"
                   style="margin: 20px auto"
          >
            <h3>Contribution</h3>
            <el-form-item prop="title" style="margin: 20px;" label="Title:">
              <el-input type="text"
                        v-model="contributionForm.title"
                        auto-complete="off"
                        placeholder="your title here"
                        class="input"
                        maxlength="50"
                        show-word-limit
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="summary" style="margin: 20px;" label="Summary:">
              <el-input type="textarea"
                        v-model="contributionForm.summary"
                        auto-complete="off"
                        placeholder="your summary here"
                        class="input"
                        maxlength="800"
                        :rows="3"
                        show-word-limit
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="topic" style="margin:20px;" label="Topic:">
              <el-transfer @change="handleChange" v-model="contributionForm.topic" :data="topic"
                           :titles="['Topics remained','Topics selected']"
                           :button-texts="['Remove','Add']"></el-transfer>
            </el-form-item>
            <el-form-item
              class="details"
              v-for="(author,index) in contributionForm.authors"
              :key="author.key"
              style="text-align: left;">
              <label slot="label" style="margin-left: 350px">Author{{index+1}}&nbsp;:&nbsp;</label>
              <el-form-item :prop="'authors.'+index+'.name'"
                            :rules="{required:true,message:'Author\'s name is required',trigger:'blur'}" label="Name:"
                            style="display:inline-flex;margin-left: 170px;">
                <el-input v-model="author.name" style="width: 350px" placeholder="author's name here"></el-input>
              </el-form-item>
              <el-form-item :prop="'authors.'+index+'.office'"
                            :rules="{required:true,message:'Author\'s office is required',trigger:'blur'}"
                            label="Office:"
                            style="display: inline-flex;margin-left: 170px">
                <el-input v-model="author.office" style="width: 350px" placeholder="author's office here"></el-input>

              </el-form-item>
              <el-form-item :prop="'authors.'+index+'.region'"
                            :rules="{required:true,message:'Author\'s region is required',trigger:'blur'}"
                            label="Region:"
                            style="display: inline-flex;margin-left: 170px">
                <el-cascader
                  v-model="author.region"
                  style="width: 350px"
                  :options="options"
                  :show-all-levels="false"
                  filterable
                ></el-cascader>
              </el-form-item>
              <el-form-item :prop="'authors.'+index+'.email'"
                            :rules="[{required:true,message:'Author\'s email is required',trigger:'blur'},{ type: 'email', message: 'invalid email form', trigger: ['blur', 'change'] }]"
                            label="Email:"
                            style="display: inline-flex;margin-left: 170px">
                <el-input v-model="author.email" style="width:350px" placeholder="author's email here"></el-input>
                <el-button @click.prevent="removeAuthor(author)" :disabled=!index type="danger">Delete</el-button>
              </el-form-item>
            </el-form-item>
            <el-form-item>
              <el-button @click="addAuthor">add a author</el-button>
              <el-button @click="resetForm">reset</el-button>
            </el-form-item>
            <el-form-item prop="file" style="margin: 20px;" label="File:">
              <el-upload
                ref="upload"
                action
                :limit=1
                :on-exceed="handleExceed"
                :auto-upload="false"
                accept=".pdf"
                :on-change="fileChange"
                :file-list="fileList">
                <el-button size="small" plain>选择文件</el-button>
                <div slot="tip" class="el-upload__tip"><p style="font-size: 13px">only pdf file which is less than
                  1M</p></div>
              </el-upload>
            </el-form-item>
          </el-form>
          <el-button type="primary" @click="contribute(contributionForm)">Contribute</el-button>
        </el-collapse-item>
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:10%;padding-top: 20px ">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  export default {
    name: "Contribution",
    data() {
      const dataValid = (rule, value, callback) => {
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'))
        }
        return callback()
      };
      const fileValid = (rule, value, callback) => {
        const isPDF = value.type === 'application/pdf';
        const isLt1M = value.size / 1024 / 1024 < 1;
        if (!isPDF) {
          this.$message({
            message: 'You can only upload PDF file!',
            type: 'warning',
            showClose: true,
            duration: 0
          });
          return callback(new Error('Only PDF file'))
        }
        if (!isLt1M) {
          this.$message({
            message: 'You can only upload file no more than 1M!',
            type: 'warning',
            showClose: true,
            duration: 0
          });
          return callback(new Error('No more than 1M'))
        }
        return callback()
      };
      return {
        informationData: [{
          conferenceFullName: '',
          abbreviation: '',
          place: '',
          startDate: '',
          endDate: '',
          deadline: '',
          releaseTime: ''
        }], details: [{
          name: '',
          office: '',
          region: '',
          email: ''
        }],
        contributionForm: {
          title: '',
          summary: '',
          topic: [],
          authors: [{
            fullName: '',
            office: '',
            region: [],
            email: '',
          }],
          file: null
        },
        rules: {
          title: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          summary: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          topic: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          file: [{required: true, message: '', trigger: 'blur'}, {validator: fileValid, trigger: 'change'}],
        },
        visible: false,
        loading: false,
        detailsVisible: false,
        conferenceFullName: this.$store.state.selectedConference.conference,
        activeName: '1',
        topic: [],
        fileList: [],
        cursor: 0,
        options: this.$store.state.options,
        id:-1,
      }
    },
    methods: {
      test(item) {
        console.log(item)
      },
      confirm(formName) {
        console.log(this.$refs[formName]);
        this.$refs[formName][0].validate(valid => {
          if (valid) {
            this.detailsVisible = false;
            this.$refs[formName][0].resetFields();
          }
        });
      }
      , showDetails(item) {
        this.detailsVisible = true;
        this.cursor = this.contributionForm.authors.indexOf(item)
      },
      handleChange(value, direction, movedKeys) {
        console.log(this.contributionForm.topic);
      }, resetForm() {
        let length = this.contributionForm.authors.length;
        for (let i = 0; i < length; i++) {
          this.contributionForm.authors[i].name = '';
          this.contributionForm.authors[i].office = '';
          this.contributionForm.authors[i].region = [];
          this.contributionForm.authors[i].email = '';
        }
      }, removeAuthor(item) {
        let index = this.contributionForm.authors.indexOf(item);
        if (index !== -1) {
          this.contributionForm.authors.splice(index, 1)
        }
      }, addAuthor() {
        this.contributionForm.authors.push({
          fullName: '',
          office: '',
          region: [],
          email: '',
          key: Date.now()
        });
        this.details.push({
          fullName: '',
          office: '',
          region: '',
          email: '',
          key: Date.now()
        })
      },
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      }, contribute(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            let formData = new FormData();
            formData.append('id',this.id);
            formData.append('conferenceFullName', this.conferenceFullName);
            formData.append('title', this.contributionForm.title);
            formData.append('summary', this.contributionForm.summary);
            formData.append('authors',JSON.stringify(this.contributionForm.authors));
            formData.append('topics',JSON.stringify(this.contributionForm.topic));
            formData.append('file', this.contributionForm.file);
            let jsonData = {};
            formData.forEach(((value, key) => jsonData[key] = value));
            let axios1 = require('axios');
            axios1.defaults.baseURL = '/api';
            axios1.defaults.withCredentials = true;
            axios1.defaults.headers.post['Content-Type'] = "multipart/form-data;charset=UTF-8";
            axios1.post('/submit', formData
            )
              .then(resp => {
                if (resp.status === 200) {
                  this.$message({
                    message: 'Successful contribution!',
                    type: 'success'
                  });
                  this.$router.replace('/personalInformation')
                }
              }).catch(error => {
                if (error.status === 403) {
                  this.$store.commit('logout');
                } else {
                  this.$message({
                    message: 'Contribution is not available right now!',
                    type: "error",
                    showClose: true,
                    duration: 0
                  })
                }
              }
            )
          }
        })
      }, fileChange(file, fileList) {
        this.contributionForm.file = fileList[0].raw;
      }, handleExceed() {
        this.$message({
          message: 'You can only submit one file in a single contribution!',
          type: 'warning',
          showClose: true,
          duration: 0
        })
      },
    },

    created() {
      console.log(this.$route.params.test);
      const temp=this.$route.params.test;
      if (temp!=null) {
        this.id=temp.id;
        this.contributionForm.title = temp.title;
        this.contributionForm.summary = temp.summary;
        this.contributionForm.topic = JSON.parse(temp.topics);
        this.contributionForm.authors = JSON.parse(temp.authors);
        this.fileList = [{name: temp.fileName+'.pdf', url: temp.path}];
      }
      this.$axios.post('/searchConference', {
        conferenceFullName: this.conferenceFullName
      })
        .then(resp => {
          console.log(resp);
          this.informationData[0].conferenceFullName = resp.data.fullName;
          this.informationData[0].abbreviation = resp.data.abbreviation;
          this.informationData[0].place = resp.data.place;
          this.informationData[0].startDate = resp.data.startDate.substring(0, 10);
          this.informationData[0].endDate = resp.data.endDate.substring(0, 10);
          this.informationData[0].deadline = resp.data.deadline.substring(0, 10);
          this.informationData[0].releaseTime = resp.data.releaseTime.substring(0, 10);
          const temp=JSON.parse(resp.data.topics);
          const length=temp.length;
          for(let i=0;i<length;i++){
            this.topic.push({
              key:temp[i],
              label:temp[i]
            })
          }
        });
    }
  }
</script>

<style scoped>
  #base_contribution {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  .contribution_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 20px 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  p {
    margin: 0;
  }

  .information_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .el-collapse {
    border-radius: 15px;
    margin: auto;
    width: 70%;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    padding: 15px;
    background-color: white;
  }

  .details /deep/ .el-form-item__content {
    line-height: 40px;
  }

   /deep/ .el-form--label-top .el-form-item__label {
    width: 60px;
  }

   /deep/ .el-form-item__content{
    line-height: normal;
  }

   /deep/ .el-transfer-panel{
    text-align: left;
  }
</style>
