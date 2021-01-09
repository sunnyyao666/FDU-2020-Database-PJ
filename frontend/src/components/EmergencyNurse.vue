<template>
  <div id="base_emergencynurse"  style="overflow:auto;">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Act as a Emergency Nurse</p>
          <p style="margin: 0;padding:1% 0px;text-align: left;background-color: white;">You can <b>receive patient</b>
            here(remember to <b>fill in the form</b> with their <b>basic information and disease grade</b>) and you can
            <b>view
              the information of the patients who meet certain requirement(s).</b></p>
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
      <el-collapse v-model="activeName" accordion>
        <el-collapse-item name="1">
          <template slot="title">
            <h3>Receiving&nbsp;</h3><em class="head-icon el-icon-s-home"></em>
          </template>
          <el-form :model="ReceivingForm"
                   :rules="rules"
                   class="receiving_container"
                   label-position="left"
                   label-width="0px"
                   :ref="ReceivingForm"
                   v-loading="loading"
                   style="margin: 20px auto">

            <h3 class="login_title">Receiving a patient</h3>
            <el-form-item label="Name:" prop="name" style="margin: 20px;">
              <el-input type="text"
                        v-model="ReceivingForm.name"
                        auto-complete="off"
                        placeholder="patient's name here"
                        class="input"
                        style="width: 500px">
              </el-input>
            </el-form-item>
            <el-form-item label="Information:" prop="information" style="margin: 20px">
              <el-input type="text"
                        v-model="ReceivingForm.information"
                        auto-complete="off"
                        placeholder="patient's information here"
                        class="input"
                        style="width: 500px"
              >
              </el-input>
            </el-form-item>
            <el-form-item label="Condition:" prop="condition" style="margin: 20px">
              <el-radio-group v-model="ReceivingForm.condition" style="margin-top: 15px">
                <el-radio label="1">mild</el-radio>
                <el-radio label="2">severe</el-radio>
                <el-radio label="3">critical</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item prop="result" style="margin: 20px">
              <template slot="label">Nucleic&nbsp;acid&nbsp;test&nbsp;result:</template>
              <el-radio-group v-model="ReceivingForm.result"  style="margin-top: 15px">
                <el-radio label="0">negative</el-radio>
                <el-radio label="1">positive</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="Date" prop="date" style="margin: 20px">
              <el-date-picker
                v-model="ReceivingForm.date"
                type="datetime"
                placeholder="choose the time of the nucleic acid test" style="width: 310px">
              </el-date-picker>
            </el-form-item>
          </el-form>
          <el-button type="primary" @click="receive(ReceivingForm)">Submit</el-button>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <h3>Search&nbsp;</h3><em class="head-icon el-icon-search"></em>
          </template>
          <el-form :model="searchingForm"
                   :rules="rules1"
                   class="searching_container"
                   :ref="searchingForm"
                   v-loading="loading"
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
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:5% ">Copyright © 2021 FDU18SS YHT&LCH</el-footer>
  </div>
</template>

<script>
  import moment from "moment"
  export default {
    name: "EmergencyNurse",
    data() {const dataValid = (rule, value, callback) => {
      if (!value || value === '') {
        return callback(new Error('Can\'t be empty'))
      }

    };
      return {
        options:[{value:0,label:'leave hospital'},{
          value:1,
          label:'mild treating area'
        },{
          value: 2,
          label: 'severe treating area'
        },{
          value: 3,
          label: 'critical treating area'
        },{
          value: 4,
          label: 'died'
        },{
          value:5,
          label:'isolation area'
        },{
          value:7,
          label:'mild'
        },{
          value:8,
          label:'severe'
        },{
          value:9,
          label:'critical'
        }
        ],
        activeName: '1',
        visible: false,
        ReceivingForm: {
          name: '',
          information: '',
          condition: '',
          result:'',
          date:''
        },
        searchData:[],
        currentPage: 1,
        pageSize: 10,
        loading: false,
        rules: {
          name: [{required: true, message: '', trigger: 'blur'}],
          information: [{required: true, message: '', trigger: 'blur'}],
          condition: [{required: true, message: '', trigger: 'blur'}],
          result:[{required: true, message: '', trigger: 'blur'}],
          date:[{required: true, message: '', trigger: 'blur'}],
        },
        searchingForm:{
          filter:''
        },
        rules1:{
          filter:[{required: true, message: '', trigger: 'blur'},{validator: dataValid, trigger: 'blur'}]
        }
      }
    },
    methods: {
      receive(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/emergencyNurseAddPatient', {
              name: this.ReceivingForm.name,
              information: this.ReceivingForm.information,
              condition: this.ReceivingForm.condition,
              result:this.ReceivingForm.result,
              createTime:moment(this.ReceivingForm.date).format("YYYY-MM-DD HH:mm:ss")
            }).then(resp => {
              if (resp.status === 200) {
                this.$message({
                  message: 'successful receiving!',
                  type: 'success'
                });
                location.reload()
              }
            })
          }
        })
      },
      search(formName){
        this.$refs[formName].validate(valid=>{
          if(valid){
            this.$axios.post('/emergencyNurseGetPatients',{
              filter:this.searchingForm.filter
            }).then(resp=>{
              this.searchData=resp.data;
              const length = this.searchData.length;
              for (let i = 0; i < length; i++) {
                if (this.searchData[i].condition === 1) {
                  this.searchData[i].condition = "mild";
                } else if (this.searchData[i].condition === 2) {
                  this.searchData[i].condition = "severe"
                } else if (this.searchData[i].condition === 3) {
                  this.searchData[i].condition = "critical"
                }else if (this.searchData[i].condition === 4) {
                  this.searchData[i].condition = "died"
                }else if (this.searchData[i].condition === 0) {
                  this.searchData[i].condition = "leave hospital"
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
            })
          }
        })
      },handleSizeChange: function (size) {
        this.pageSize = size;
      },
      handleCurrentChange: function (currentPage) {
        this.currentPage = currentPage;
      },
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      },
      getTreatingArea(area){
        if(area===1){
          return "mild treating area"
        }else if(area===2){
          return "severe treating area"
        }else if(area===3){
          return "critical treating area"
        }
      },getCondition(condition){
        if(condition===1){
          return "mild"
        }else if(condition===2){
          return "severe"
        }else if(condition===3){
          return "critical"
        }else if(condition===4){
          return "died"
        }else if(condition===0){
          return "leave hospital"
        }
      }
    }
  }
</script>

<style scoped>
  #base_emergencynurse {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  .receiving_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 20px 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .searching_container{
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 20px 35px;
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
