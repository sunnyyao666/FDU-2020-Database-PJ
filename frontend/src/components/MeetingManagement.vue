<template>
  <div id="base_meeting" style="overflow: auto">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Select your meeting</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can <strong>choose the conference
            you want to participate in</strong> in this page and after you <strong>select the role you want to act</strong>, you will
            <strong>enter the meeting.</strong></p>
        </el-col>
        <el-col :span="3"  style="padding-top: 20px">
          <router-link to="personalInformation" style="text-decoration: none;" onmouseover="this.style.fontWeight='bold'"
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
      <el-form class="conference_participated">
        <h3>Conference you have participated</h3>
        <el-table :data="valid" stripe style="margin-bottom: 20px">
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
                <el-form-item label="character" class="character">
                  <el-tooltip >
                    <div slot=content>related topics:
                      <span v-for="(tp,i) of props.row.topics" :key='i'>{{props.row.topics[i]}} </span>
                    </div>
                  <el-button type="danger" plain size="medium" v-if="props.row.chair===true"
                             @click="chair(props.$index)">
                    Chair
                  </el-button>
                  </el-tooltip>
                  <el-tooltip >
                    <div slot=content>related topics:
                      <span v-for="(tp,i) of props.row.PCTopics" :key='i'>{{props.row.PCTopics[i]}} </span>
                    </div>
                  <el-button type="success" plain size="medium" v-if="props.row.PC===true"
                               @click="PC(props.$index)">
                    PC
                  </el-button>
                  </el-tooltip>
                  <el-tooltip >
                    <div slot=content>related topics:
                      <span v-for="(tp,i) of props.row.authorTopics" :key='i'>{{props.row.authorTopics[i]}} </span>
                    </div>
                  <el-button type="info" plain size="medium" v-if="props.row.author===true"
                                @click="author(props.$index)">
                    Author
                  </el-button>
                  </el-tooltip>


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
            label="Chair">
            <template slot-scope="props">
              <em class="el-icon-user"></em>
              <span style="margin-left: 10px">{{ props.row.creatorName }}</span>
            </template>
          </el-table-column>
          <!-- <el-table-column
           label="status">
            <template slot-scope="scope">
               <span style="margin-left: 10px" v-if="scope.row.valid===true">passed</span>
               <span style="margin-left: 10px" v-if="scope.row.status==='failed'">failed</span>
               <span style="margin-left: 10px" v-if="scope.row.status==='opened'">opened</span>
               <span style="margin-left: 10px" v-if="scope.row.status===''">-</span>
             </template>

           </el-table-column>   -->


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
    computed: {
      valid: function () {
        var tep = [];
        for (var i = 0; i < this.$store.state.conferences.length; i++) {
          if (this.$store.state.conferences[i].valid) {
            tep.push(this.$store.state.conferences[i]);
          }
        }

        return tep;
      },
      // chairTopics:function(){
      //   var tep=[];
      //    for (var i = 0; i < this.$store.state.conferences.length; i++) {

      //       tep.push(this.$store.state.conferences[i].topics);

      //   }
      //   return tep;
      // },
      // PCTopics:function(){
      //   var tep=[];
      //    for (var i = 0; i < this.$store.state.conferences.length; i++) {

      //       tep.push(this.$store.state.conferences[i].PCTopics);

      //   }
      //   return tep;
      // },
      // authorTopics:function(){
      //   var tep=[];
      //    for (var i = 0; i < this.$store.state.conferences.length; i++) {

      //       tep.push(this.$store.state.conferences[i].authorTopics);

      //   }
      //   return tep;
      // },
    },
    // beforeMount(){
    //   for(var i=0;i<this.$store.state.conferences.length;i++){
    //     if(this.$store.state.conferences[i].valid){
    //       this.validConferences.push(this.$store.state.conferences[i]);
    //     }
    //   }
    //   console.log(validConferences);
    // },

    methods: {
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      },
      chair(index) {
        this.$store.commit('setSelectedConference', { conference:this.$store.state.conferences[index].fullName ,type:"chair"});
        //console.log(this.$store.state.selectedConference);
        this.$router.push('/chair');
      },
      PC(index) {
        this.$store.commit('setSelectedConference',{ conference:this.$store.state.conferences[index].fullName ,type:"PC"});
        //console.log(this.$store.state.selectedConference);
        this.$router.push('/pcmember');
      },
      author(index) {
        this.$store.commit('setSelectedConference',{ conference:this.$store.state.conferences[index].fullName ,type:"author"});
        //  console.log(this.$store.state.selectedConference);
        this.$router.push('/author');
      }

    }
  }

</script>

<style scoped>
  #base_meeting {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }

  .conference_participated {
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

  .el-form-item {
    width: 100%;
  }


  .application_container {
    border-radius: 15px;
    background: #fff;
    background-clip: padding-box;
    margin: 0px auto;
    width: 800px;
    padding: 10px 35px 0 35px;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .transition-box {
    margin-bottom: 10px;
    width: 200px;
    height: 100px;
    border-radius: 4px;

    background-color: #F27070;
    text-align: center;
    color: #fff;
    padding: 40px 20px;
    box-sizing: border-box;
    margin-right: 20px;
  }
  p {
    margin: 0;
  }


</style>
