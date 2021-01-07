<template>
  <div id="base_PCMemeber" style="overflow: auto">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Act as an Author</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can <strong>edit your previous
            contribution</strong> in this page, you can also <strong>read the status of your contribution</strong> and
            <strong>know the comment once it is published.</strong>In the new version, if your thesis is <strong>not
              accepted in
              the first period</strong>, you can <strong>rebuttal to the reviewer</strong>. Note that you
            <strong>only</strong> have <strong>one chance for each thesis</strong></p>
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
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <h3>Contribution record&nbsp;</h3><em class="header-icon el-icon-question"></em>
          </template>
          <el-form class="history_container" style="margin: 20px auto">
            <h3>Contribution record</h3>
            <el-table :data="theses">

              <el-table-column
                prop="title"
                label="Title"
                align="center"></el-table-column>
              <el-table-column
                prop="summary"
                label="Summary"
                align="center"
              ></el-table-column>
              <el-table-column
                label="Status"
                align="center" width="210px">
                <template slot-scope="prop">
                  <el-button type="primary" plain
                             icon="el-icon-loading" v-if="auditing&&!submitting||(rechanging1&&!rechanging2)">in audit
                  </el-button>
                  <el-button type="primary" plain icon="el-icon-loading" v-if="submitting&&!auditing">
                    contributing
                  </el-button>
                  <el-button type="success" plain icon="el-icon-check" v-if="rebutting">
                    first result released
                  </el-button>
                  <el-button type="primary" plain icon="el-icon-loading" v-if="rechanging2&&!rechanging1">in second
                    audit
                  </el-button>
                  <el-button type="danger" plain icon="el-icon-close"
                             v-if="rechanging1&&rechanging2&&!theses[prop.$index].accepted">
                    denied
                  </el-button>
                  <el-button type="success" plain icon="el-icon-check"
                             v-if="rechanging1&&rechanging2&&theses[prop.$index].accepted">
                    accepted
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                label="Handle"
                align="center">
                <template slot-scope="prop">
                  <el-button type="text" @click="showEdit(prop.$index)" :disabled="auditing">Edit</el-button>
                  <el-button type="text" @click="showScore(prop.$index)"
                             :disabled="!(rebutting||rechanging1&&rechanging2)">Show score
                  </el-button>

                </template>
              </el-table-column>
              <el-table-column label="Rebuttal" align="center">
                <template slot-scope="props">
                  <el-button type="primary" @click="showRebuttal(props.$index)"
                             :disabled="!(!theses[props.$index].accepted&&rebutting)||theses[props.$index].rebuttal!==null">
                    Rebuttal
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
          <el-dialog title="Your score" :visible.sync="scoreVisible" :modal-append-to-body="false">
            <el-table :data="score" stripe>
              <el-table-column label="Score" align="center">
                <template slot-scope="props">
                  <el-rate disabled v-model="score[props.$index].score" :max="4" :colors="colors" show-text
                           :texts="texts"></el-rate>
                </template>
              </el-table-column>
              <el-table-column prop="comment" label="Comment" align="center"></el-table-column>
              <el-table-column prop="confidence" label="Confidence" align="center"></el-table-column>
            </el-table>
          </el-dialog>
          <el-dialog :modal-append-to-body="false" :visible.sync="rebuttalVisible" title="Rebuttal"
          >
            <el-form :model="rebuttalForm" :rules="rules" label-position="top"
                     label-width="0px"
                     :ref="rebuttalForm"
                     style="margin: 20px auto;padding-bottom: 20px"
            >
              <el-form-item prop="rebuttal" label="Rebuttal">
                <el-input v-model="rebuttalForm.rebuttal" auto-complete="off"
                          placeholder="your rebuttal here"></el-input>
              </el-form-item>
              <el-tooltip content="Warning! You only have one chance to submit your rebuttal!">
                <el-button type="primary" @click="popover=true">
                  Submit
                </el-button>
              </el-tooltip>
            </el-form>
          </el-dialog>
          <el-dialog :modal-append-to-body="false" :visible.sync="popover" title="Confirm">
            <p>Are you sure you want to submit this rebuttal? You will not be able to modify it if you submit it</p>
            <el-button @click="popover=false">Cancel</el-button>
            <el-button @click="rebuttal(rebuttalForm)" type="primary">Confirm</el-button>
          </el-dialog>
        </el-collapse-item>
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:10%;padding-top: 20px ">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  import pdf from 'vue-pdf'

  export default {
    name: "Author",
    data() {
      return {
        informationData: [{
          conferenceFullName: '',
          abbreviation: '',
          place: '',
          startDate: '',
          endDate: '',
          deadline: '',
          releaseTime: ''
        }],
        visible: false,
        conferenceFullName: this.$store.state.selectedConference.conference,
        activeName: '1',
        historyData: [],
        editVisible: false,
        contributionForm: {
          title: '',
          summary: '',
          topic: [],
          authors: [{
            name: '',
            office: '',
            region: [],
            email: '',
          }],
        },
        file: '',
        theses: [],
        src: '',
        submitting: false,
        auditing: false,
        scoreVisible: false,
        score: [],
        colors: {2: '#99A9BF', 3: '#F7BA2A', 4: '#FF9900'},
        texts: ['-2:reject', '-1:weak reject', '1:weak accept', '2:accept'],
        rebuttalVisible: false,
        index: null,
        rebuttalForm: {
          rebuttal: null,
        },
        rules: {
          rebuttal: [{required: true, message: '', trigger: 'blur'}],
        },
        rechanging1: false,
        rechanging2: false,
        rebutting: false,
        popover:false,
      }
    }, components: {
      pdf
    },
    methods: {
      rebuttal(formName) {
        this.$refs[formName].validate(valid => {
            if (valid) {
              this.$axios.post('/rebut', {
                thesisID: this.theses[this.index].id,
                text: this.rebuttalForm.rebuttal
              }).then(resp => {
                this.$message({
                  message: 'Successful submission!',
                  type: 'success'
                });
                location.reload();
              })
            }else{
              this.$message({
                message:'rebuttal can\'t be empty',
                type:'error'
              })
            }
          }
        )
      },
      showRebuttal(index) {
        this.rebuttalVisible = true;
        this.index = index;
      },
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      }, showScore(index) {
        this.scoreVisible = true;
        this.score = this.theses[index].pcAudits
      },
      showEdit(index) {
        this.$router.push({name: 'Contribution', params: {test: this.theses[index]}})
      }
    }, created: function () {
      this.$axios.post('/searchConference', {
        conferenceFullName: this.conferenceFullName
      })
        .then(resp => {
          console.log(resp);
          this.submitting = resp.data.submitting;
          this.auditing = resp.data.auditing;
          this.informationData[0].conferenceFullName = resp.data.fullName;
          this.informationData[0].abbreviation = resp.data.abbreviation;
          this.informationData[0].place = resp.data.place;
          this.informationData[0].startDate = resp.data.startDate.substring(0, 10);
          this.informationData[0].endDate = resp.data.endDate.substring(0, 10);
          this.informationData[0].deadline = resp.data.deadline.substring(0, 10);
          this.informationData[0].releaseTime = resp.data.releaseTime.substring(0, 10);
          this.rechanging1 = resp.data.rechanging1;
          this.rechanging2 = resp.data.rechanging2;
          this.rebutting = resp.data.rebutting;
        });
      this.$axios.post('updateUser', {})
        .then(resp => {
          console.log(resp.data.theses);
          for (let i = 0; i < resp.data.theses.length; i++) {
            if (resp.data.theses[i].conferenceFullName === this.$store.state.selectedConference.conference) {
              this.theses.push(resp.data.theses[i])
            }
          }
        })
    }
  }
</script>

<style scoped>
  #base_PCMemeber {
    background: url("../assets/background/newbackground.jpg") no-repeat center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
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

  p {
    margin: 0;
  }
</style>
