<template>
  <div id="base_administrator" style="overflow:auto;">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Act as an Administrator</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can <strong>see and audit all the
            conference application</strong> here, and you may do something more in our later edition.</p>
        </el-col>
        <el-col :span="3"  style="padding-top: 20px">
          <el-popover placement="bottom" v-model="visible">
            <p>Are you sure you want to log out?</p>
            <el-row style="text-align: right;margin-top:7px;">
              <el-button size="mini" @click="visible=false">No</el-button>
              <el-button type="primary" size="mini" @click="logout">Yes</el-button>
            </el-row>
            <el-button plain style="margin-top: 20px;" slot="reference">
              Log out
            </el-button>
          </el-popover>
        </el-col>
      </el-row>
    </el-header>
    <el-main style="min-height: 70%">
      <el-form class="list_conference">
        <h3>Conference to be audited</h3>
        <el-table :data="this.$store.state.conferences" stripe style="margin-bottom: 20px">
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
                  <span>{{props.row.startDate.substring(0,10)}}</span>
                </el-form-item>
                <el-form-item label="end date" prop="endDate">
                  <span>{{props.row.endDate.substring(0,10)}}</span>
                </el-form-item>
                <el-form-item label="deadline" prop="deadline">
                  <span>{{props.row.deadline.substring(0,10)}}</span>
                </el-form-item>
                <el-form-item label="release time" prop="releaseTime">
                  <span>{{props.row.releaseTime.substring(0,10)}}</span>
                </el-form-item>
                <el-form-item label="topics" prop="topics" style="width:100%">
                    <el-tag v-for="(tp,i) of props.row.topics" style="min-width:50px;margin-right:10px; text-align:center"
                  :key="i" size="small">{{props.row.topics[i]}}</el-tag>
                    </el-form-item>
              </el-form>
              <el-main>
                <div style="margin: 0 auto;width: 30%">
              <el-button type="success" plain size="medium" @click="pass(props.$index, $store.state.conferences)">
                pass
              </el-button>
              <el-button type="danger" plain size="medium" @click="fail(props.$index, $store.state.conferences)">
                fail
              </el-button>
                </div>
              </el-main>
            </template>
          </el-table-column>

          <el-table-column
            label="full name"
            prop="fullName">
          </el-table-column>
          <el-table-column
            label="applicant">
            <template slot-scope="scope">
              <em class="el-icon-user"></em>
              <span style="margin-left: 10px">{{ scope.row.creatorName }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </el-main>
    <el-footer style="text-align: center;height:10%;padding-top: 20px ">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        visible: false,
      }
    },
    beforeMount() {
      this.$axios.post('/listConferences', {
        text: 'admin'
      })
        .then(resp => {
          if (resp.status === 200) {
            for(var i=0;i<resp.data.length;i++){
              resp.data[i].topics=JSON.parse(resp.data[i].topics)
            }
            this.$store.commit('setConferences', resp.data);


          }
        })
        .catch(error => {
          this.$message({
            message: error.message,
            type: "error",
            showClose: true,
            duration: 0,
          });
          this.$store.commit('logout');
        })
    },

    methods: {
      handleChange(val) {
      },
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      },
      pass(index, rows) {

        this.$axios.post('/auditConferenceApplication', {
          conferenceFullName: rows[index].fullName,
          passed: true
        })
          .then(resp => {
            if (resp.status === 200) {
              rows.splice(index, 1);
              this.$store.commit('setConferences', rows);
              this.$message.success('Successful operation!')
            }
          })
          .catch(error => {
            this.message({
              message: 'Meeting audit is not available right now. Please try again later',
              type:'error',
              showClose:true,
              duration:0
            })

          })

        rows[index].status = "passed";

      },
      fail(index, rows) {
        this.$axios.post('/auditConferenceApplication', {
          conferenceFullName: rows[index].fullName,
          passed: false
        })
          .then(resp => {
            if (resp.status === 200) {
              rows.splice(index, 1)
              this.$store.commit('setConferences', rows);
              this.$message.success('Successful operation!')
            }
          })
          .catch(error => {
            this.message({
              message: 'Meeting audit is not available right now. Please try again later',
              type:'error',
              showClose:true,
              duration:0
            })
          })
      }
    }

  }
</script>

<style scoped>
  #base_administrator {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  .application {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .demo-table-expand {
    font-size: 0;
  }

  .demo-table-expand /deep/ label {
    width: 90px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

  .applicant {
    line-height: 48px;
    text-align: center;
  }

  .assess {
    width: 50%;
    margin: 0 auto;
    text-align: center;
  }

  .list_conference{
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0px auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }


</style>

