<template>
  <div id="base_register">
    <el-header style="height: 20%;padding-left: 5em;">
      <p style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
        Create a HardChair Account</p>
      <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">Note that <strong>the most common reason
        for failing to create an account is an incorrect email address</strong>, so please type your email address correctly.
      </p>
    </el-header>
    <el-form :model="registerForm" :rules="rules" class="register_container" label-position="left"
             label-width="0px" v-loading="loading" :ref="registerForm" style="height: 70%;">
      <h3 class="register_title">Register</h3>
      <el-form-item prop="username" label=" ">
        <label class="date">Username:</label>
        <el-input type="text" v-model="registerForm.username"
                  auto-complete="off" placeholder="your username here" class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="password" label=" ">
        <label class="date">Password:</label>
        <el-input type="password" v-model="registerForm.password"
                  auto-complete="off" placeholder="your password here" class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="fullName" label=" ">
        <label class="date">Fullname:</label>
        <el-input type="text" v-model="registerForm.fullName"
                  auto-complete="off" placeholder="your fullname here" class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="email" label=" ">
        <label class="date">Email:</label>
        <el-input type="text" v-model="registerForm.email"
                  auto-complete="off" placeholder="your e-mail address here" class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="office" label=" ">
        <label class="date">Office:</label>
        <el-input type="text" v-model="registerForm.office" auto-complete="off" placeholder="your office here"
                  class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="region" label=" ">
        <label class="date">Region:</label>
        <el-cascader
          v-model="registerForm.region"
          style="width: 350px"
          :options="options"
          @change="handleChange"
          :show-all-levels="false"
          filterable
        ></el-cascader>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 40%;background: dodgerblue;border-radius: 4px;height: 40px;"
                   v-on:click="register(registerForm)">register
        </el-button>
      </el-form-item>
      <p>
        Already have an account?
        <router-link to="login" style="text-decoration: none;color: red;">
          Log in
        </router-link>
        here.
      </p>
    </el-form>
    <el-footer style="text-align: center;height: 10%;padding-top: 20px;">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  export default {
    name: 'Register',
    data() {
      const dataValid = (rule, value, callback) => {
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'))
        }
        return callback()
      }
      const usernameCheck = (rule, value, callback) => {
        const reg = /^[-a-zA-Z]{1}([a-zA-Z0-9]|[-]|[_]){4,31}$/;
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'));
        } else if (!reg.test(value)) {
          return callback(new Error('账号只能包含字母，数字或两种特殊字符（-_）且只能以字母或-开头，长度为5-32个字符'));
        } else {
          return callback();
        }
      };
      const passwordCheck = (rule, value, callback) => {
        const reg = /^(?![\d]+$)(?![a-zA-Z]+$)(?![_-]+$)[\da-zA-Z_-]{6,32}$/;
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'));
        } else if (!reg.test(value)) {
          return callback(new Error('密码需要字母，数字或者特殊字符（-_）至少包含两种，长度为6-32个字符'));
        } else if (value.indexOf(this.registerForm.username) != -1) {
          return callback(new Error('密码中不得含有用户名'));
        } else {
          return callback();
        }
      };
      const emailCheck = (rule, value, callback) => {
        const reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
        if (!value || value === '') {
          return callback(new Error('Can\'t be empty'));
        } else if (!reg.test(value)) {
          return callback(new Error('邮箱格式错误！请检查您的输入'));
        } else {
          return callback();
        }
      };

      return {
        registerForm: {
          username: '',
          password: '',
          fullName: '',
          email: '',
          office: '',
          region: [],
        },
        options: this.$store.state.options,

        rules: {
          // blur 失去鼠标焦点时触发验证
          username: [{required: true, message: '', trigger: 'blur'}, {validator: usernameCheck, trigger: 'blur'}],
          password: [{required: true, message: '', trigger: 'blur'}, {validator: passwordCheck, trigger: 'blur'}],
          fullName: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          email: [{required: true, message: '', trigger: 'blur'}, {validator: emailCheck, trigger: 'blur'}],
          office: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
          region: [{required: true, message: '', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
        },
        loading: false
      }
    },
    methods: {
      handleChange(value) {
      },
      register(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/register', {
                username: this.registerForm.username,
                password: this.registerForm.password,
                fullName: this.registerForm.fullName,
                email: this.registerForm.email,
                office: this.registerForm.office,
                region: this.registerForm.region
              }
            )
              .then(resp => {
                // 根据后端的返回数据修改
                if (resp.status === 200 && resp.data.hasOwnProperty("id")) {
                  // 跳转到login
                  this.$message({
                    message: 'Successful registration!',
                    type: "success",
                  })
                  this.$router.replace('/login')
                } else {
                  this.$message({
                    message: 'Register failed, please try again later!',
                    type: "error",
                    showClose: true,
                    duration: 0,
                  });
                }
              })
              .catch(error => {
                this.$message({
                  message: 'Username has been registered, please change your username!',
                  type: "error",
                  showClose: true,
                  duration: 0,
                });
              })
          } else {
            this.$message({
              message: 'Make sure all information required is filled in and meet the requirements!',
              type: "warning",
              showClose: true,
              duration: 0
            })
          }
        })
      }
    }
  }
</script>

<style scoped>
  #base_register {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  p {
    margin: 0;
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

  .register_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0px auto;
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
</style>
