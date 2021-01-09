<template>
  <div id="base_matron" style="overflow: auto">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Act as a Matron</p>
          <p style="margin: 0;padding:1% 0px;text-align: left;background-color: white;">You can search for the
            information
            of <b>patients who meet certain requirement(s)</b> and view the information of <b>the matron and ward nurse
              who
              belongs
              to this treating area and the patients that are taken care of by them.</b>You can also <b>add or remove
              the ward nurses of this treating area</b> and <b>view the information of every sickbed in this treating
              area.</b></p>
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
    <el-main style="min-height: 70%;overflow: unset">
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item name="1">
          <template slot="title">
            <h3>Search&nbsp;</h3><em class="header-icon el-icon-search"></em>
          </template>
          <el-form :model="searchingForm"
                   :rules="rules"
                   class="searching_container"
                   :ref="searchingForm"
                   style="margin: 20px auto">
            <h3 class="login_title">Searching for certain patients</h3>
            <el-form-item>
              <el-select v-model="searchingForm.filter" placeholder="choose your requirement">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
              <el-button type="primary" @click="search(searchingForm)" icon="el-icon-search">Search</el-button>
            </el-form-item>
            <el-table :data="searchData.slice((currentPage-1)*pageSize,currentPage*pageSize)" stripe>
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-form label-position="left" class="details">
                    <el-form-item label="name">
                      <span>{{props.row.name}}</span>
                    </el-form-item>
                    <el-form-item label="information">
                      <span>{{props.row.information}}</span>
                    </el-form-item>
                    <el-form-item label="treating area">
                      <span>{{getTreatingArea(props.row.area)}}</span>
                    </el-form-item>
                    <el-form-item label="disease grade">
                      <span>{{props.row.condition}}</span>
                    </el-form-item>
                    <el-form-item label="state">
                      <el-table :data="props.row.states">
                        <el-table-column prop="temperature" label="Temperature" align="center"></el-table-column>
                        <el-table-column prop="symptom" label="Symptom" align="center"></el-table-column>
                        <el-table-column prop="createTime" label="Record Time" align="center"></el-table-column>
                      </el-table>
                    </el-form-item>
                    <el-form-item label="test result">
                      <el-table :data="props.row.tests">
                        <el-table-column prop="result" label="Result" align="center">
                        </el-table-column>
                        <el-table-column prop="createTime" label="Test time" align="center">
                        </el-table-column>
                      </el-table>
                    </el-form-item>
                  </el-form>
                </template>
              </el-table-column>
              <el-table-column
                prop="name"
                label="name">
              </el-table-column>
              <el-table-column
                prop="information"
                label="information"
              >
              </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-sizes="[5,10,20,40]" :page-size="pageSize" layout="total,sizes,prev,pager,next,jumper"
                           :total="searchData.length">
            </el-pagination>
          </el-form>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <h3>Nurse&nbsp;Information&nbsp;</h3><em class="header-icon el-icon-info"></em>
          </template>
          <el-form class="searching_container" style="margin: 20px auto">
            <h3>Information of nurses in this treating area</h3>
            <el-table :data="informationData">
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-table :data="getPatientList(props.$index)">
                    <el-table-column label="name" prop="patient.name">
                    </el-table-column>
                    <el-table-column label="information" prop="patient.information">
                    </el-table-column>
                    <el-table-column label="condition">
                      <template slot-scope="prop2">
                        {{getCondition(prop2.row.patient.condition)}}
                      </template>
                    </el-table-column>
                    <el-table-column label="test result">
                      <template slot-scope="props1">
                        <el-table :data="props1.row.patient.tests">
                          <el-table-column label="result">
                            <template slot-scope="props2">
                              {{getResult(props2.row.result)}}
                            </template>
                          </el-table-column>
                          <el-table-column label="time" prop="createTime">

                          </el-table-column>
                        </el-table>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </el-table-column>
              <el-table-column prop="username" label="Name" align="center">
              </el-table-column>
              <el-table-column prop="information" label="Information" align="center">
              </el-table-column>
              <el-table-column prop="authorities[0].authority" label="Position" align="center">
              </el-table-column>
              <el-table-column align="center">
                <template slot-scope="props1">
                  <el-button type="danger" :disabled="props1.row.authorities[0].sickbeds.length!==0"
                             @click="deleteNurse(props1.$index)">delete
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </el-collapse-item>
        <el-collapse-item name="3">
          <template slot="title">
            <h3>Add&nbsp;Nurse&nbsp;</h3><em class="header-icon el-icon-zoom-in"></em>
          </template>
          <el-form class="searching_container" style="margin: 20px auto">
            Username:
            <el-input v-model="username"></el-input>
          </el-form>
          <el-button type="primary" @click="addNurse">Add</el-button>
        </el-collapse-item>
        <el-collapse-item name="4">
          <template slot="title">
            <h3>Ward&nbsp;Information&nbsp;</h3><em class="header-icon el-icon-info"></em>
          </template>
          <el-table :data="wards">
            <el-table-column>
              <template slot-scope="props">
                <el-table :data="wards[props.$index].sickbeds">
                  <el-table-column label="ID" prop="id" align="center"></el-table-column>
                  <el-table-column label="Status" align="center">
                    <template slot-scope="props1">
                      <div v-if="props1.row.patient===null">empty</div>
                      <div v-if="props1.row.patient!==null">
                        Name:{{props1.row.patient.name}}
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
        <el-collapse-item name="5">
          <template slot="title">
            <h3>Confirm&nbsp;Transfer&nbsp;</h3><em class="header-icon el-icon-check"></em>
          </template>
          <el-table :data="confirmData">
            <el-table-column label="Name" prop="name" align="center"></el-table-column>
            <el-table-column label="Information" prop="information" align="center"></el-table-column>
            <el-table-column align="center">
              <template slot-scope="props">
                <el-button @click="confirm(props.$index)" type="success">Confirm</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse-item>
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:5% ">Copyright © 2021 FDU18SS YHT&LCH</el-footer>
  </div>
</template>

<script>
  import moment from "moment"

  export default {
    name: "Matron",
    data() {
      return {
        visible: false,
        activeName: '1',
        searchingForm: {
          filter: ''
        },
        options: [{
          value: 0,
          label: 'all patients in this treating area'
        }, {
          value: 1,
          label: 'able to leave hospital'
        }, {
          value: 2,
          label: 'ready to transfer'

        }, {
          value: 3,
          label: 'newly transferred'
        }], searchData: [],
        currentPage: 1,
        pageSize: 10,
        rules: {
          filter: [{required: true, message: '', trigger: 'blur'}]
        }, informationData: [], result: '', date: '', username: '',
        wards: '',confirmData:''
      }
    }, methods: {
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      }, deleteNurse(index) {
        this.$axios.post('/matronUpdateNurse', {
          username: this.informationData[index].username,
          filter: 0
        }).then(resp => {
          location.reload()
        })
      }, addNurse() {
        this.$axios.post('/matronUpdateNurse', {
          username: this.username,
          filter: 1
        }).then(resp => {
          location.reload()
        })
      }, modifyCondition(index) {
        this.$axios.post('/doctorUpdateCondition', {
          patientID: this.searchData[index].id,
          condition: this.searchData[index].condition
        }).then(resp => {
          location.reload()
        })
      }, addNewSheet(index) {
        this.$axios.post('/doctorCreateTest', {
          patientID: this.searchData[index].id,
          result: this.result,
          createTime: moment(this.date).format("YYYY-MM-DD HH:mm:ss")
        }).then(resp => {
          location.reload()
        })
      }, handleSizeChange: function (size) {
        this.pageSize = size;
      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage;
      }, search(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/matronGetPatients', {
              filter: this.searchingForm.filter
            }).then(resp => {
              this.searchData = resp.data;
              const length = this.searchData.length;
              for (let i = 0; i < length; i++) {
                if (this.searchData[i].condition === 1) {
                  this.searchData[i].condition = "mild";
                } else if (this.searchData[i].condition === 2) {
                  this.searchData[i].condition = "severe"
                } else if (this.searchData[i].condition === 3) {
                  this.searchData[i].condition = "critical"
                }
                const length1 = this.searchData[i].tests.length;
                for (let j = 0; j < length1; j++) {
                  if (this.searchData[i].tests[j].result === 0) {
                    this.searchData[i].tests[j].result = "negative"
                  } else if (this.searchData[i].tests[j].result === 1) {
                    this.searchData[i].tests[j].result = "positive"
                  }
                }
              }
              console.log(resp.data)
            })
          }
        })
      }, getTreatingArea(area) {
        if (area === 1) {
          return "mild treating area"
        } else if (area === 2) {
          return "severe treating area"
        } else if (area === 3) {
          return "critical treating area"
        }
      }, getCondition(condition) {
        if (condition === 1) {
          return "mild"
        } else if (condition === 2) {
          return "severe"
        } else if (condition === 3) {
          return "critical"
        }
      }, getResult(result) {
        if (result === 0) {
          return "negative"
        } else if (result === 1) {
          return "positive"
        }
      }, getPatientList(index) {
        console.log(index);
        return this.informationData[index].authorities[0].sickbeds
      },confirm(index){
        this.$axios.post('/matronConfirmPatient',{
          patientID:this.confirmData[index].id
        }).then(resp=>{
          location.reload()
        })
      }
    }, beforeMount() {
      this.$axios.post('/matronGetNurses', {filter: 1}).then(resp => {
        this.informationData = resp.data;
        // console.log(resp.data)
      });
      this.$axios.post('/matronGetWards', {}).then(resp => {
        this.wards = resp.data;
        // console.log(this.wards)
      });
      this.$axios.post('/matronGetPatients',{
        filter:3
      }).then(resp=>{
        this.confirmData=resp.data;
        console.log(this.confirmData)
      })
    }
  }
</script>

<style scoped>
  #base_matron {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
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

  .searching_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 20px 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .details /deep/ .el-form-item__content {
    line-height: 40px;
  }

  /deep/ .el-form--label-top .el-form-item__label {
    width: 60px;
  }

  /deep/ .el-form-item__content {
    line-height: normal;
  }

  /deep/ .el-transfer-panel {
    text-align: left;
  }

  .details {
    font-size: 0;
  }

  .details /deep/ label {
    width: 150px;
    color: #99a9bf;
  }

  .details .el-form-item {
    padding-left: 50px;
  }
</style>
