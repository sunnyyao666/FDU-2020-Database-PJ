<template>
    <div id="base_information" style="overflow: auto">
      <el-header style="height: 20%;padding-left: 5em;">
        <el-row style="background-color: white">
          <el-col :span="21">
            <p
              style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
              Modify your information</p>
            <p style="margin: 0;padding:1% 0px;text-align: left;background-color: white;">You can <b>view your personal information</b> here, and you can <b>modify your password and information</b> as well.</p>
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
              <h3>Information&nbsp;</h3><em class="head-icon el-icon-info"></em>
            </template>
            <el-table :data="information">
              <el-table-column label="Username" align="center" prop="username"></el-table-column>
              <el-table-column label="Information" align="center" prop="information"></el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item name="2">
            <template slot="title">
              <h3>Modify&nbsp;Password&nbsp;</h3><em class="head-icon el-icon-upload"></em>
            </template>
            New password:&nbsp;<el-input type="text" v-model="password" style="width: 500px"></el-input>
            <el-button type="primary" @click="newPassword">submit</el-button>
          </el-collapse-item>
          <el-collapse-item name="3">
            <template slot="title">
              <h3>Modify&nbsp;Information&nbsp;</h3><em class="head-icon el-icon-upload"></em>
            </template>
            New information:&nbsp;<el-input type="text" v-model="information1" style="width: 500px"></el-input>
            <el-button type="primary" @click="newInformation">submit</el-button>
          </el-collapse-item>
        </el-collapse>
      </el-main>
      <el-footer style="text-align: center;height:5% ">Copyright © 2021 FDU18SS YHT&LCH</el-footer>
    </div>
</template>

<script>
    export default {
        name: "Information",
        data(){
          return{
            activeName: '1',
            visible:false,
            information:[{
              username:this.$store.state.username,
              information:this.$store.state.information
            }],password:'',information1:''
          }
        },methods:{
        logout() {
          this.$message({
            message: 'Successful logout!',
            type: 'success'
          });
          this.$store.commit('logout');
          this.$router.replace('/login')
        },newPassword(){
          this.$axios.post('/updateInfo',{
            password:this.password,
            information:this.$store.state.information
          }).then(resp=>{
            this.$store.commit('logout');
            this.$router.replace('/login')
          })
        },newInformation(){
          this.$axios.post('/updateInfo',{
            password:null,
            information:this.information1
          }).then(resp=>{
            this.$store.commit('logout');
            this.$router.replace('/login')
          })
        }
      }
    }
</script>

<style scoped>
  #base_information{
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
</style>
