<template>
  <div id="base_personalInformation" style="overflow: auto;">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Your Personal Information</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">Your can check <strong>your personal
            information</strong> in the following table, and you may <strong>edit some of them</strong> in our later edition.</p>
        </el-col>
        <el-col :span="3">
          <el-popover placement="bottom" v-model="visible">
            <p>Are you sure you want to log out?</p>
            <el-row style="text-align: right;margin-top:7px;">
              <el-button size="mini" @click="visible=false">No</el-button>
              <el-button type="primary" size="mini" @click="logout">Yes</el-button>
            </el-row>
            <el-button plain slot="reference" style="margin-top: 30px">
              Log out
            </el-button>
          </el-popover>
        </el-col>
      </el-row>
    </el-header>
    <el-main style="min-height: 70%">
      <el-collapse accordion v-model="activeName">
        <el-collapse-item name="1">
          <template slot="title">
            <h3>Personal Information&nbsp;</h3><em class="header-icon el-icon-info"></em>
          </template>
          <el-form class="personalInformation_container"
                   label-position="left"
                   label-width="0px"
                   style="margin: 20px auto">
            <h3 class="personalInformation_title">Personal Information</h3>
            <el-table
              :data="tableData"
              style="width: 800px;margin: auto">
              <el-table-column
                prop="fullName"
                label="Fullname"
                align="center">
              </el-table-column>
              <el-table-column
                prop="username"
                label="Username"
                align="center">
              </el-table-column>
            </el-table>
            <el-table
              :data="tableData"
              style="width: 800px;margin: auto"
            >
              <el-table-column
                prop="office"
                label="Office"
                align="center">
              </el-table-column>
              <el-table-column
                prop="email"
                label="Email"
                align="center">
              </el-table-column>
              <el-table-column
                prop="region"
                label="Region"
                align="center">
              </el-table-column>
            </el-table>
            <p style="padding-top:10px">
              Want to apply for a meeting?
              <router-link to="application" style="text-decoration: none;color: red;">
                click here
              </router-link>
            </p>
            <p style="padding-top:10px">
              Want to see your work?
              <router-link to="MeetingManagement" style="text-decoration: none;color: red;" type="text">
                click here
              </router-link>
            </p>
            <p style="padding-top:10px">
              Want to see status of your meeting application?
              <el-button @click="table=true" style="text-decoration: none;color: red; padding: 0;" type="text">
                click here
              </el-button>
              <el-drawer
                title="Your Applications"
                :visible.sync="table"
                direction="rtl"
                append-to-body
                size="30%">
                <el-table :data="status">
                  <el-table-column property="fullName" label="full name" width="150"></el-table-column>
                  <el-table-column label="status" width="250">
                    <template slot-scope="props">
                      <span v-if="props.row.applying">waiting for validation</span>
                      <span v-else-if="props.row.submitting&&!props.row.auditing"> submmitting</span>
                      <span v-else-if="!props.row.submitting&&props.row.auditing"> auditing</span>
                      <span v-else-if="props.row.submitting&&props.row.auditing"> stop contributing/finished</span>
                      <span v-else-if="!props.row.submitting&&!props.row.auditing&&props.row.valid"> valid</span>
                      <span v-else>invalid</span>
                    </template>
                  </el-table-column>
                </el-table>
              </el-drawer>
            </p>
            <p style="padding-top:10px">
              Want to contribute? check the conference list
              <el-button @click="contribute=true" style="text-decoration: none;color: red;padding:0;" type="text">
                click here
              </el-button>
            </p>
            <el-drawer
              :visible.sync="contribute"
              direction="ltr"
              append-to-body
              size="70%"
              style="overflow: auto"
            >
              <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Remember:
                a conference's chair
                <strong> can not </strong>contribute to the same conference!</p>
              <el-table :data="contributionConferences"
              style="max-height:600px;overflow:auto"
              >
                <el-table-column type="expand">
                  <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                      <el-form-item label="abbreviation" prop="abbreviation">
                        <span>{{props.row.abbreviation}}</span>
                      </el-form-item>
                      <el-form-item label="place" prop="place">
                        <span>{{props.row.place}}</span>
                      </el-form-item>
                      <el-form-item label="start date" prop="startDate">
                        <span>{{props.row.startDate.substring(0, 10)}}</span>
                      </el-form-item>
                      <el-form-item label="end date" prop="endDate">
                        <span>{{props.row.endDate.substring(0, 10)}}</span>
                      </el-form-item>
                      <el-form-item label="deadline" prop="deadline">
                        <span>{{props.row.deadline.substring(0, 10)}}</span>
                      </el-form-item>
                      <el-form-item label="release time" prop="releaseTime">
                        <span >{{props.row.releaseTime.substring(0, 10)}}</span>
                      </el-form-item>
                      <el-form-item label="topics" prop="topics">
                        <el-tag v-for="(tp,i) of props.row.topics" style="min-width:50px;margin-right:10px; text-align:center"
                  :key="i" size="small">{{props.row.topics[i]}}</el-tag>
                      </el-form-item>
                      <el-form-item class="character">
                        <el-button type="info" plain size="medium" @click="startContribute(props.$index)">
                          Contribute
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>

                <el-table-column
                  label="Fullname"
                >
                  <template slot-scope="props">

                    <span style="margin-left: 10px">{{ props.row.fullName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="chair">
                  <template slot-scope="props">
                    <em class="el-icon-user"></em>
                    <span style="margin-left: 10px">{{ props.row.creatorName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="status">
                  <template slot-scope="props">
                    <span v-if="props.row.submitting&&!props.row.auditing" style="margin-left: 10px"> submmitting</span>
                      <span v-else-if="!props.row.submitting&&props.row.auditing" style="margin-left: 10px"> auditing</span>
                      <span v-else-if="props.row.submitting&&props.row.auditing" style="margin-left: 10px"> stop contributing/finished</span>
                      <span v-else-if="!props.row.submitting&&!props.row.auditing&&props.row.valid" style="margin-left: 10px"> valid</span>
                      <span v-else>invalid</span>

                  </template>
                </el-table-column>
              </el-table>
            </el-drawer>
          </el-form>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <h3>Invited Record&nbsp;</h3><em class="header-icon el-icon-message-solid"></em>
          </template>
          <el-form class="history_container" style="margin: 20px auto">
            <h3>Invited Record</h3>
            <el-table :data="historyData.slice((currentPage-1)*pageSize,currentPage*pageSize)" stripe>
              <el-table-column
                prop="username"
                label="Inviter"
                align="center">
              </el-table-column>
              <el-table-column
                prop="conference"
                label="Conference"
                align="center">
              </el-table-column>
               <el-table-column

                label="topics"
                align="center">
                <template slot-scope="props">
                 <el-checkbox-group v-model="seletedTopics[props.$index]" align='left'>
                <el-checkbox v-for="(tp,i) of props.row.topics" :key=i :label="tp">{{props.row.topics[i]}}</el-checkbox>

                </el-checkbox-group>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
              >
                <template slot-scope="scope">
                  <el-button type="success" plain @click="accept(scope.$index)">Accept</el-button>
                  <el-button type="danger" plain @click="deny(scope.$index)">Deny</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                           :current-page="currentPage" :page-sizes="[5,10,20,40]" :page-size="pageSize"
                           layout="total,sizes,prev,pager,next,jumper" :total="historyData.length"></el-pagination>
          </el-form>
        </el-collapse-item>
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:10%;padding-top: 20px">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  export default {
    name: "PersonalInformation",
    data() {
      return {
        activeName: '1',
        visible: false,
        contribute: false,
        table: false,
        tableData: [{
          username: this.$store.state.username,
          email: this.$store.state.email,
          office: this.$store.state.office,
          fullName: this.$store.state.fullName,
          region: this.$store.state.region,
        }],
        contributionConferences: [],
        historyData: [],
        currentPage: 1,
        pageSize: 10,
        seletedTopics:[]
      }
    },
    computed: {
      status: function () {
        var tep = [];
        if (this.$store.state.conferences !== null) {
          for (var i = 0; i < this.$store.state.conferences.length; i++) {

            if (this.$store.state.conferences[i].creatorName === this.$store.state.username) {
              tep.push(this.$store.state.conferences[i]);
            }
          }
        }
        return tep;
      }
    },
    beforeMount() {
      this.$axios.post('/listConferences', {
        text: 'submission'
      })
        .then(resp => {
          if (resp.status === 200) {
            for(var i = 0; i < resp.data.length; i++){
              resp.data[i].topics=JSON.parse(resp.data[i].topics)
            }
            this.contributionConferences = resp.data

          this.$axios.post('/updateUser', {})
            .then(resp => {
          if (resp.status === 200) {
            console.log(resp.data)
            for(var i = 0; i < resp.data.conferences.length; i++){
              resp.data.conferences[i].topics=JSON.parse(resp.data.conferences[i].topics)
            }
             //  console.log(resp.data.conferences[i]);
            for (let i = 0; i < resp.data.authorities.length; i++) {
           //   console.log(resp.data.authorities[i])
              if (resp.data.authorities[i].authority === "Undetermined PC Member") {

                this.historyData.unshift({
                  username: resp.data.authorities[i].inviter,
                  conference: resp.data.authorities[i].conferenceFullName,
                  topics:JSON.parse(resp.data.authorities[i].topics)
                });
                this.seletedTopics.push([])
              }
            }
            //  console.log(this.seletedTopics)
            for (var i = 0; i < resp.data.conferences.length; i++) {
              this.$set(resp.data.conferences[i], 'chair', false);
              this.$set(resp.data.conferences[i], 'PC', false);
              this.$set(resp.data.conferences[i], 'author', false);
              this.$set(resp.data.conferences[i],'PCTopics',[]);
              this.$set(resp.data.conferences[i],'authorTopics',[]);
            }
            var tep = this.contributionConferences
            //console.log(tep)
            for (var i = 0; i < resp.data.authorities.length; i++) {
              switch (resp.data.authorities[i].authority) {
                case 'Chair':

                  for (var j = 0; j < resp.data.conferences.length; j++) {
                    if (resp.data.authorities[i].conferenceFullName === resp.data.conferences[j].fullName) {
                      resp.data.conferences[j].chair = true;
                     // resp.data.conferences[j].relatedTopics=JSON.parse(resp.data.authorities[i].topics);
                    }
                  }
                  break;
                case 'PC Member':
                  var found=false;
                  for(var j = 0; j < resp.data.conferences.length; j++){
                    if(resp.data.authorities[i].conferenceFullName === resp.data.conferences[j].fullName){
                      resp.data.conferences[j].PC = true;
                      found=true;
                      break;
                    }

                  }
                  if(!found){
                  for (var j = 0; j < tep.length; j++) {
                    if (tep[j] != null && resp.data.authorities[i].conferenceFullName === tep[j].fullName) {
                      this.$set(tep[j], 'PC', true);
                      this.$set(tep[j],'PCTopics',JSON.parse(resp.data.authorities[i].topics))
                      resp.data.conferences.push(tep[j])
                      break;
                    }
                  }
                  }
                  break;
                case 'Author':
                  var found=false;
                     for(var j = 0; j < resp.data.conferences.length; j++){
                    if(resp.data.authorities[i].conferenceFullName === resp.data.conferences[j].fullName){
                      resp.data.conferences[j].author = true;
                      found=true;
                      break;
                    }

                  }
                  if(!found){
                  for (var j = 0; j < tep.length; j++) {
                    if (tep[j] != null && resp.data.authorities[i].conferenceFullName === tep[j].fullName) {
                      this.$set(tep[j], 'author', true);
                      resp.data.conferences.push(tep[j]);
                      break;
                    }
                  }
                  }
                  break;
              }
            }

            //console.log(resp.data.conferences);
            this.$store.commit('setConferences', resp.data.conferences);
            console.log(this.$store.state.conferences);
          }
        })
        .catch(error => {
        //  console.log(error)
          this.$message({
            message: error.message,
            type: "error",
            showClose: true,
            duration: 0,
          });
        })
          }
        })

      // this.$axios.post('/listConferences',{
      //   text:'submission'
      // })
      //  .then(resp => {
      //  if (resp.status === 200)
      //  console.log(resp.data)
      // this.$data.contributionConferences=resp.data
      //  })
      //  console.log(this.$data.contributionConferences)
    },
    methods: {
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      },
      startContribute(index) {
        if (!this.contributionConferences[index].submitting&&!this.contributionConferences[index].auditing) {
          this.$message({
            message: "the conference has not started contribution",
            type: "error",
            showClose: true,
            duration: 0,
          });
        } else if(!this.contributionConferences[index].submitting&&this.contributionConferences[index].auditing){
             this.$message({
            message: "the conference has started auditing",
            type: "error",
            showClose: true,
            duration: 0,
          });


        }else if(this.contributionConferences[index].submitting&&this.contributionConferences[index].auditing){
             this.$message({
            message: "the conference has stopped contributing",
            type: "error",
            showClose: true,
            duration: 0,
          });
        }else if (this.contributionConferences[index].creatorName === this.$store.state.username) {
          this.$message({
            message: "a conference's chair can not contribute to the same conference !",
            type: "error",
            showClose: true,
            duration: 0,
          });
        } else {
          this.$store.commit('setSelectedConference', {
            conference: this.contributionConferences[index].fullName,
            type: "contribution"
          });

          this.$router.push('/Contribution');
        }
      }, handleSizeChange: function (size) {
        this.pageSize = size;
      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage;
      },
      accept(index) {
       // console.log(this.seletedTopics[index])
       // console.log(this.historyData[index].conference)
      if(!this.seletedTopics[index].length){
        this.$message({
              message:'Should choose at least one topic!',
              type:'error'
              })
              return 0;
      }
      let topicJSON=JSON.stringify(this.seletedTopics[index])
     // console.log(topicJSON);
        this.$axios.post('/auditPCInvitationApplication', {
          conferenceFullName: this.historyData[index].conference,
          text: JSON.stringify(this.seletedTopics[index])
        })
        .then(resp => {
          if (resp.status === 200) {
            this.$message({
              message:'Success Accept!',
              type:'success'
              })
            location.reload()
          }
        })
      },
      deny(index) {
        // this.$axios.post('/searchConference',{
        //   conferenceFullName: this.historyData[index].conference
        // })
        // .then(resp=>{
        //   console.log(resp)
        // })
        this.$axios.post('/auditPCInvitationApplication', {
          conferenceFullName: this.historyData[index].conference,
          text: "false"
        }).then(resp => {
          if (resp.status === 200) {
            this.$message({
              message:'Success Accept!',
              type:'success'
              })
            location.reload()
          }
        })
      }
    }
  }
</script>

<style scoped>
  #base_personalInformation {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  .personalInformation_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    height: 65%;
    margin: 0 auto 50px;
  }

  .personalInformation_title {
    margin: 0px auto 10px auto;
    text-align: center;
    color: #505458;
  }

  .history_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  div.character.el-form-item {
    width: 100%
  }

  .el-table_1_column_2 {
    font-weight: bolder;
    font-size: 20px;
  }

  .demo-table-expand {
    font-size: 0;
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

  .demo-table-expand /deep/ label {
    width: 100px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

  .el-form-item {
    width: 100%;
  }

  .el-drawer.ltr {

    overflow-y: scroll;
  }
</style>


