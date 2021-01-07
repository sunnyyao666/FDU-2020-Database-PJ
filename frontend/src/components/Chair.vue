<template>
  <div id="base_chair" style="overflow: auto">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Act as a Chair</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can invite someone as a
            <strong>PC
              Member</strong> by searching his/her <strong>fullname</strong> and have your <strong>invite
              history</strong> here, you can also <strong>start
              the contribution</strong> in this page.</p>
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
      <p style="text-align:left">status of the conference:
        <el-tag size='medium' effect="dark">{{status}}</el-tag>
      </p>
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
          <el-button v-if="!contributing&&!auditing" @click="startContribution" type="primary">Start Contribution
          </el-button>
          <el-button disabled v-if="!(!contributing&&!auditing)" type="primary">Contribution has started</el-button>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <h3>Invitation&nbsp;</h3><em class="header-icon el-icon-user-solid"></em>
          </template>
          <el-form :model="chair" v-loading="loading" :ref="chair" class="invite_container" :rules="rules"
                   style="margin:20px auto">
            <h3 class="chair_title">Invite others to become PC Member</h3>
            <el-form-item prop="fullName" style="text-align: left">
              <el-input v-model="chair.fullName" style="width:200px"></el-input>
              <el-button type="primary" icon="el-icon-search" @click="search(chair)">search</el-button>
              <strong>(by fullname)</strong>
            </el-form-item>
            <el-table :data="inviteData.slice((currentPage-1)*pageSize,currentPage*pageSize)" stripe v-if="invitation"
                      ref="multipleTable" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="55">
              </el-table-column>
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-form label-position="left" class="details">
                    <el-form-item label="Fullname">
                      <span>{{props.row.fullName}}</span>
                    </el-form-item>
                    <el-form-item label="Username">
                      <span>{{props.row.username}}</span>
                    </el-form-item>
                    <el-form-item label="Email">
                      <span>{{props.row.email}}</span>
                    </el-form-item>
                    <el-form-item label="Office">
                      <span>{{props.row.office}}</span>
                    </el-form-item>
                    <el-form-item label="Region">
                      <span>{{props.row.region[props.row.region.length-1]}}</span>
                    </el-form-item>
                  </el-form>
                </template>
              </el-table-column>
              <el-table-column
                prop="fullName"
                label="Fullname">
              </el-table-column>
              <el-table-column
                prop="username"
                label="Username"
              >
              </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-sizes="[5,10,20,40]" :page-size="pageSize" layout="total,sizes,prev,pager,next,jumper"
                           :total="inviteData.length" v-if="invitation">
            </el-pagination>
          </el-form>
          <el-button v-if="invitation" @click="invite" type="primary">invite</el-button>
        </el-collapse-item>
        <el-collapse-item name="3">
          <template slot="title">
            <h3>Start Audit&nbsp;</h3><em class="header-icon el-icon-question"></em>
          </template>
          <el-form class="history_container" style="margin: 20px auto">
            <h3>Invite History</h3>
            <el-table :data="historyData.slice((currentPage1-1)*pageSize1,currentPage1*pageSize1)" stripe>
              <el-table-column
                prop="username"
                label="Username"
                align="center">
              </el-table-column>
              <el-table-column
                prop="authority"
                label="status"
                align="center"
              >
                <template slot-scope="scope">
                  <el-button v-if="scope.row.authority=='Undetermined PC Member'" size="medium" type="primary" plain
                             icon="el-icon-loading"
                             style="width: 158px">to be
                    confirmed
                  </el-button>
                  <el-button v-if="scope.row.authority=='PC Member'" size="medium" type="success" plain
                             icon="el-icon-check"
                             style="width: 158px">confirmed
                  </el-button>
                  <el-button v-if="scope.row.authority=='Denied PC Member'" size="medium" type="danger" plain
                             icon="el-icon-close"
                             style="width: 158px">denied
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
                           :current-page="currentPage1" :page-sizes="[5,10,20,40]" :page-size="pageSize1"
                           layout="total,sizes,prev,pager,next,jumper" :total="historyData.length"></el-pagination>
          </el-form>
          <el-button v-if="contributing&&!auditing" @click="topicDistribution" type="primary">Start Distribution(topics
            based)
          </el-button>
          <el-button v-if="contributing&&!auditing" @click="workDistribution" type="primary">Start Distribution(work
            load based)
          </el-button>
          <el-button disabled v-if="!(contributing&&!auditing)" type="primary">Start Distribution(topics based)
          </el-button>
          <el-button disabled v-if="!(contributing&&!auditing)" type="primary">Start Distribution(work load based)
          </el-button>
        </el-collapse-item>
        <el-collapse-item name="4">
          <template slot="title">
            <h3>Audit as a PC Member&nbsp;</h3><em class="header-icon el-icon-question"></em>
          </template>
          <el-button :disabled="!auditing" @click="audit" type="primary">
            Check theses
          </el-button>

        </el-collapse-item>
        <el-collapse-item name="5">
          <template slot="title">
            <h3>Finish Auditing&nbsp;</h3><em class="header-icon el-icon-question"></em>
          </template>
          <el-button @click="publish" type="primary"
                     :disabled="!(!contributing&&auditing)">Finish
            Auditing
          </el-button>
        </el-collapse-item>
        <el-collapse-item name="6">
          <template slot="title">
            <h3>Stop Rebuttal&nbsp;</h3><em class="header-icon el-icon-microphone"></em>
          </template>
          <el-button type="primary" :disabled="!rebutting" @click="stopRebuttal">Stop Rebuttal</el-button>
        </el-collapse-item>
        <el-collapse-item name="7">
          <template slot="title">
            <h3>Publish Result&nbsp;</h3><em class="header-icon el-icon-document"></em>
          </template>
          <el-button type="primary" v-if="!rechanging2" :disabled="!rechanging1&&!rechanging2" @click="releaseFirst">
            Release first employment
            result
          </el-button>
          <el-button type="primary" v-if="rechanging2" :disabled="rechanging1&&rechanging2" @click="releaseSecond">
            Release final employment
            result
          </el-button>
        </el-collapse-item>
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:10%;padding-top: 20px ">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  export default {
    name: "Chair",
    data() {
      const dataValid = (rule, value, callback) => {
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'))
        }
        return callback()
      };
      return {
        chair: {
          fullName: '',
        }, rules: {
          fullName: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
        }, informationData: [{
          conferenceFullName: '',
          abbreviation: '',
          place: '',
          startDate: '',
          endDate: '',
          deadline: '',
          releaseTime: ''
        }]
        , inviteData: [],
        invitee: [],
        historyData: [],
        visible: false,
        loading: false,
        currentPage: 1,
        pageSize: 10,
        currentPage1: 1,
        pageSize1: 10,
        conferenceFullName: this.$store.state.selectedConference.conference,
        activeName: '1',
        contributing: false,
        invitation: false,
        multipleSelection: [],
        PCNum: 1,
        auditing: false,
        rechanging1: false,
        rechanging2: false,
        rebutting: false
        //status:"audting"
      }
    },
    computed: {
      status: function () {
        if (!this.contributing && !this.auditing) {
          return "Initializing"
        } else if (this.contributing && !this.auditing) {
          return "Contributing"
        } else if (!this.contributing && this.auditing) {
          return "Auditing"
        } else if (this.rebutting) {
          return "Author rebuttal"
        } else if (this.contributing && this.auditing && !this.rechanging1 && !this.rechanging2) {
          return "Auditing finished"
        } else if (this.rechanging1 && !this.rechanging2) {
          return "First discussion"
        } else if (this.rechanging2 && !this.rechanging1) {
          return "Second discussion"
        } else if (this.rechanging2 && this.rechanging1) {
          return "Conference finished"
        }


        return 0;
      }
    },
    methods: {
      stopRebuttal() {
        this.$axios.post('/endRebut', {
          conferenceFullName: this.conferenceFullName
        }).then(resp => {
          this.$message({
            message: 'Successful operation!',
            type: 'success'
          });
          location.reload();
        })
      },
      releaseFirst() {
        this.$axios.post('/releaseAcceptance1', {
          conferenceFullName: this.conferenceFullName
        }).then(resp => {
          if (resp.data) {
            this.$message({
              message: 'Successful release!',
              type: 'success'
            });
            location.reload();
          } else {
            this.$message({
              message: 'some PC member has not finished audit, please wait',
              type: 'error'
            })
          }
        })
      },
      releaseSecond() {
        this.$axios.post('releaseAcceptance2', {
          conferenceFullName: this.conferenceFullName
        }).then(resp => {
          if (resp.data) {
            this.$message({
              message: 'Successful release!',
              type: 'success'
            });
            this.$router.replace("/personalInformation")
          } else {
            this.$message({
              message: 'some PC member has not finished audit, please wait',
              type: 'error'
            })
          }
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
      startContribution() {
        this.$axios.post('/changeSubmissionState', {
          conferenceFullName: this.conferenceFullName,
          passed: true,
        }).then(resp => {
          this.$message({
            message: 'The contribution of this meeting now has started!',
            type: 'success'
          });
          this.contributing = true;
        })
      },
      search(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/searchUsers', {
              text: this.chair.fullName,
              conferenceFullName: this.conferenceFullName,
            })
              .then(resp => {
                this.inviteData = resp.data;
                this.invitation = true;
              })
          }
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
        console.log(this.multipleSelection)
      },
      invite() {
        const length = this.multipleSelection.length;
        if (length === 0) {
          this.$message({
            message: 'You should invite at least one user!',
            type: 'error'
          })
        } else {
          for (let i = 0; i < length; i++) {
            this.invitee[i] = this.multipleSelection[i].username;
          }
          this.$axios.post('/invitePCMember', {
            invitee: this.invitee,
            conferenceFullName: this.conferenceFullName,
          });
          location.reload();
          this.$message({
            message: 'successful invitation!',
            type: 'success'
          })
        }
      },
      handleSizeChange: function (size) {
        this.pageSize = size;
      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage;
      }, handleSizeChange1: function (size) {
        this.pageSize1 = size;
      },
      handleCurrentChange1: function (currentPage) {
        this.currentPage1 = currentPage;
      },
      topicDistribution() {
        if (this.contributing === false) {
          this.$message({
            message: 'You have not start contribution yet ',
            type: 'error'
          })
          return 0
        } else if (this.PCNum < 3) {
          this.$message({
            message: 'not enough PC members! You should have at least three PC members ',
            type: 'error'
          })
          return 0
        } else {
          this.$axios.post('/startAudit', {
            isPassed: true,
            conferenceFullName: this.conferenceFullName
          })
            .then(resp => {
              console.log(resp)
              switch (resp.data) {
                case "No thesis":
                  this.$message({
                    message: 'have no thesis to audit ',
                    type: 'error'
                  })

                  break;
                case "OK":
                  this.$message({
                    message: 'The audit of this meeting now has started!',
                    type: 'success'
                  })
                  this.auditing = true;
                  this.contributing = false;
                  break;
                case "Fail to distribute":
                  this.$message({
                    message: 'Fail to distribute!',
                    type: 'error'
                  })

                default:
                  this.$message({
                    message: resp.data,
                    type: 'error'
                  })
              }
            })
            .catch(error => {
              this.$message({
                message: error,
                type: 'warning',


              })

            })
        }
      },
      workDistribution() {
        if (this.contributing === false) {
          this.$message({
            message: 'You have not start contribution yet ',
            type: 'error'
          })
          return 0
        } else if (this.PCNum < 3) {
          this.$message({
            message: 'not enough PC members! You should have at least three PC members ',
            type: 'error'
          })
          return 0
        } else {
          this.$axios.post('/startAudit', {
            isPassed: false,
            conferenceFullName: this.conferenceFullName
          })
            .then(resp => {

              switch (resp.data) {
                case "No thesis":
                  this.$message({
                    message: 'have no thesis to audit ',
                    type: 'error'
                  })

                  break;
                case "OK":
                  this.$message({
                    message: 'The audit of this meeting now has started!',
                    type: 'success'
                  })
                  this.auditing = true;
                  this.contributing = false;
                  break;
                default:
                  this.$message({
                    message: 'error!',
                    type: 'error'
                  })
              }

            })
            .catch(error => {
              this.$message({
                message: error,
                type: 'warning',


              })
            })
        }
      },
      audit() {
        this.$router.push('/pcmember');
      },
      publish() {
        this.$axios.post('/endAudit', {
          conferenceFullName: this.conferenceFullName
        })
          .then(resp => {
            if (resp.data) {
              this.$message({
                message: 'success',
                type: 'success'
              })
              this.contributing = true;
              location.reload();
            } else {
              this.$message({
                message: 'some PC member has not finished audit, please wait',
                type: 'error'
              })
            }


          })
          .catch(error => {
            this.$message({
              message: error,
              type: 'warning',


            })
          })

      }
    },
    created: function () {
      this.$axios.post('/searchConference', {
        conferenceFullName: this.conferenceFullName
      })
        .then(resp => {
          console.log(resp.data)
          this.informationData[0].conferenceFullName = resp.data.fullName;
          this.informationData[0].abbreviation = resp.data.abbreviation;
          this.informationData[0].place = resp.data.place;
          this.informationData[0].startDate = resp.data.startDate.substring(0, 10);
          this.informationData[0].endDate = resp.data.endDate.substring(0, 10);
          this.informationData[0].deadline = resp.data.deadline.substring(0, 10);
          this.informationData[0].releaseTime = resp.data.releaseTime.substring(0, 10);
          this.contributing = resp.data.submitting;
          this.auditing = resp.data.auditing;
          this.rechanging1 = resp.data.rechanging1;
          this.rechanging2 = resp.data.rechanging2;
          this.rebutting = resp.data.rebutting;
        });
      this.$axios.post('listInviteHistory', {
        conferenceFullName: this.conferenceFullName
      }).then(resp => {
        this.historyData = resp.data;
        for (var i = 0; i < resp.data.length; i++) {
          if (resp.data[i].authority === 'PC Member') {
            this.PCNum++
          }

        }
      })
    }
  }

</script>

<style scoped>
  #base_chair {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  p {
    margin: 0;
  }

  .invite_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
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

  .details {
    font-size: 0;
  }

  .details /deep/ label {
    width: 90px;
    color: #99a9bf;
  }

  .details .el-form-item {
    padding-left: 50px;
  }
</style>
