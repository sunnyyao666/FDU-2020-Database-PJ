<template>
    <div id="base_wardnurse" style="overflow: auto">
      <el-header style="height: 20%;padding-left: 5em;">
        <el-row style="background-color: white">
          <el-col :span="21">
            <p
              style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
              Act as a Ward Nurse</p>
            <p style="margin: 0;padding:1% 0px;text-align: left;background-color: white;">You can search for the
              information
              of <b>patients who meet certain requirement(s).</b>You can also <b>record the information of the patient</b> here.</p>
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
                          <el-table-column prop="createTime" label="Record Time" align="center" sortable></el-table-column>
                        </el-table>
                      </el-form-item>
                      <el-form-item label="test result">
                        <el-table :data="props.row.tests">
                          <el-table-column prop="result" label="Result" align="center">
                          </el-table-column>
                          <el-table-column prop="createTime" label="Test time" align="center" sortable>
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
              <h3>Record Information&nbsp;</h3><em class="header-icon el-icon-info"></em>
            </template>
            <el-table :data="patientInfo">
              <el-table-column type="expand">
                <template slot-scope="props">
                  <el-form :model="information"
                           :rules="rules"
                           :ref="informationData"
                           style="margin: 20px auto">
                    <el-form-item label="Temperature:">
                      <el-input type="text" v-model="information.temperature" style="width: 500px"></el-input>
                    </el-form-item>
                    <el-form-item label="Symptom:">
                      <el-input type="text" v-model="information.symptom" style="width: 500px"></el-input>
                    </el-form-item>
                    <el-form-item label="Time:">
                      <el-date-picker v-model="information.createTime" type="datetime"></el-date-picker>
                    </el-form-item>
                    <el-button type="primary" @click="submit(props.$index)">submit</el-button>
                  </el-form>
                </template>
              </el-table-column>
              <el-table-column label="name" prop="name" align="center"></el-table-column>
              <el-table-column label="information" prop="information" align="center"></el-table-column>

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
        name: "WardNurse",
      data() {
        return {
          activeName: '1',
          visible: false,
          searchingForm: {
            filter: ''
          },
          options: [{
            value: 0,
            label: 'all patients in this treating area'
          }, {
            value: 1,
            label: 'able to leave hospital'
          }], searchData: [],
          currentPage: 1,
          pageSize: 10,
          rules: {
            filter: [{required: true, message: '', trigger: 'blur'}]
          }, informationData: [], result: '', date: '',patientInfo:'',information:{
            temperature:'',
            symptom:'',
            createTime:''
          }
        }
      },methods:{
        logout() {
          this.$message({
            message: 'Successful logout!',
            type: 'success'
          });
          this.$store.commit('logout');
          this.$router.replace('/login')
        }, handleSizeChange: function (size) {
          this.pageSize = size;
        },submit(index){
          console.log(this.information.symptom);
          this.$axios.post('/wardNurseUpdateInfo',{
            patientID:this.patientInfo[index].id,
            temperature:this.information.temperature,
            symptom: this.information.symptom,
            createTime:moment(this.information.createTime).format("YYYY-MM-DD HH:mm:ss")
          }).then(resp=>{
            location.reload()
          })
        },
        handleCurrentChange: function (currentPage) {
          this.currentPage = currentPage;
        },search(formName){
          this.$refs[formName].validate(valid => {
            if (valid) {
              this.$axios.post('/wardNurseGetPatients', {
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
                console.log(resp)
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
        },
      },beforeMount() {
          this.$axios.post('/wardNurseGetPatients',{filter:0}).then(resp=>{
            this.patientInfo=resp.data
          })
      }
    }
</script>

<style scoped>
  #base_wardnurse {
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
