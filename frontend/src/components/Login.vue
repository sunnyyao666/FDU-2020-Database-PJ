<template>
  <div id="base_login">
    <el-header style="height: 20%;padding-left: 5em;">
      <p style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
        Login in to IHospitalMS</p>
      <p style="margin: 0;padding:1% 0px;text-align: left;background-color: white;">IHospitalMS uses cookies for user
        authentication. To use IHospitalMS, you should allow your browser to save cookies from ihospitalms.org.</p>
    </el-header>
    <el-form :model="loginForm"
             :rules="rules"
             class="login_container"
             label-position="left"
             label-width="0px"
             v-loading="loading"
             :ref="loginForm"
             style="height: 65%;margin-bottom: 50px">
      <h3 class="login_title">Login</h3>
      <el-form-item prop="username" style="margin: 20px;" label=" ">
        <label class="date">Username:</label>
        <el-input type="text"
                  v-model="loginForm.username"
                  auto-complete="off"
                  placeholder="your username here"
                  width="350px"
                  class="input"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="password" style="margin:20px;" label=" ">
        <label class="date">Password:</label>
        <el-input type="password"
                  v-model="loginForm.password"
                  auto-complete="off"
                  placeholder="your password here"
                  width="350px"
                  class="input"
        >
        </el-input>
      </el-form-item>
      <el-form-item style="width: 100%;">
        <el-button type="primary"
                   style="width: 300px;background: dodgerblue;border-radius: 4px;height: 40px;"
                   v-on:click="login(loginForm)">login
        </el-button>
      </el-form-item>
    </el-form>
    <el-footer style="text-align: center;height:5% ">Copyright © 2021 FDU18SS YHT&LCH</el-footer>
  </div>

</template>

<script>
  export default {
    name: 'Login',
    data() {
      const dataValid = (rule, value, callback) => {
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'))
        }

        return callback()
      }
      return {
        loginForm: {
          username: '',
          password: ''
        },
        rules: {
          username: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          password: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}]
        },
        loading: false
      }
    },
    methods: {
      login(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/login', {
                username: this.loginForm.username,
                password: this.loginForm.password
              }
            )
              .then(resp => {
                if (resp.status === 200 && resp.headers.hasOwnProperty("token")) {
                  this.$message({
                    message: 'Successful log in!',
                    type: "success"
                  });
                  console.log(resp);

                  const position=resp.data.authorities[0].authority;
                  if(position==="doctor"){
                    this.$router.push('/Doctor')
                  }
                  if(position==="emergencyNurse"){
                    this.$router.push('/EmergencyNurse')
                  }
                  if(position==="matron"){
                    this.$router.push('/Matron')
                  }
                  if(position==="wardNurse"){
                    this.$router.push('/WardNurse')
                  }
                  console.log(resp.data.authorities[0].authority);
                  this.$store.commit('login', resp.headers);
                  this.$store.commit('setUserDetails', resp.data);
                  // if (resp.data.authorities.length > 0 && resp.data.authorities[0].authority === "Admin") {
                  //   this.$store.commit('setAdmin');
                  //   this.$router.push('/ManagerAssessment');
                  //
                  // } else {
                  //   this.$router.push('/personalInformation');
                  // }
                } else {
                  this.$message({
                    message: 'Login failed, please try again later!',
                    type: "error",
                    showClose: true,
                    duration: 0,
                  });
                  this.$store.commit('logout');
                }
              })
              .catch(error => {
                this.$message({
                  message: 'Wrong username or password, please check them!',
                  type: "error",
                  showClose: true,
                  duration: 0,
                });
                this.$store.commit('logout');
              })
          } else {
            this.$message({
              message: 'Make sure all information required is filled in!',
              type: "warning",
              showClose: true,
              duration: 0
            })
          }
        })
      },

    }
  }
</script>

<style scoped>
  #base_login {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  p {
    margin: 0;
  }

  body {
    margin: 0px;
    padding: 0px;
  }

  .date {
    float: left;
    width: 0;
    padding-right: 12px;
    text-align: left;
  }

  .input {
    width: 350px;
    padding: 0;
  }

  .login_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0 auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 10px auto;
    text-align: center;
    color: #505458;
  }

  input {
    width: 300px;
    height: 24px;
    border: 2px solid #ccc;
    border-radius: 4px;
  }
</style>
