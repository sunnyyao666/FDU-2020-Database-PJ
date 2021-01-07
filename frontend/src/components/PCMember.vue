<template>
  <div id="base_PCMemeber" style="overflow: auto">
    <el-header style="height: 20%;padding-left: 5em;">
      <el-row style="background-color: white">
        <el-col :span="21">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            Act as a PCMember</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">You can <strong>examine the
            contributions you are distributed</strong>, you can <strong>read the title and the summary</strong>, also
            you can <strong>download
              the PDF file</strong> here. In the new version, you can <strong>discuss with other reviewers</strong>, and
            <strong>change your grade before the result released for the first time</strong> or <strong>after reading
              the rebuttal form the author</strong>. Note that you should <strong>give
              a mark, a comment, and a confidence for each
              contribution</strong> and <strong>you only have one chance to change your grade once you can change
              them.</strong></p>
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
            <h3>Grade&nbsp;</h3><em class="header-icon el-icon-document"></em>
          </template>
          <el-main class="information_container" style="margin: 20px auto;width: 1000px">
            <h3>Contribution distributed</h3>
            <el-table :data="theses" style="margin: 20px auto;padding-bottom: 20px;width: 900px;" stripe>
              <el-table-column prop="title" label="Title" align="center"></el-table-column>
              <el-table-column prop="summary" label="Summary" align="center"></el-table-column>
              <el-table-column align="center" label="Forum">
                <template slot-scope="props">
                  <el-button type="primary" @click="goToForum(props.$index)" icon="el-icon-chat-line-round" circle
                             :disabled="((rechanging1===rechanging2)&&!rebutting)||theses[props.$index].accepted">
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="Grade">
                <template slot-scope="props">
                  <el-button type="primary" @click="openGrade(props.$index)" :disabled="graded(props.$index)"
                             icon="el-icon-data-analysis" circle>
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="Rebuttal">
                <template slot-scope="props">
                  <el-button type="primary" :disabled="theses[props.$index].rebuttal===null||!rechanging2"
                             icon="el-icon-microphone"
                             circle @click="showRebuttal(props.$index)">
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="Preview">
                <template slot-scope="props">
                  <el-button type="primary" @click="preview(props.$index)" icon="el-icon-view" circle></el-button>
                </template>
              </el-table-column>
              <el-table-column align="center" label="Download">
                <template slot-scope="props">
                  <el-button @click="download(props.$index)" icon="el-icon-download" circle type="primary"></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-main>
          <el-dialog :modal-append-to-body="false" :visible.sync="rebuttalVisible" title="Rebuttal">
            {{rebuttal}}
          </el-dialog>
          <el-dialog :modal-append-to-body="false" :visible.sync="previewVisible" title="Preview" width="70%">
            <div style="text-align: right;">
              <el-button type="primary" @click.stop="previousPage">previous</el-button>
              <el-button type="primary" @click.stop="nextPage">next</el-button>
              <span
                style="font-size:20px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{pdfPage}}/{{pageCount}}&nbsp;&nbsp;Pages</span>
            </div>
            <pdf :src="src" :page="pdfPage" @num-pages="pageCount=$event" @page-loaded="pdfPage=$event"></pdf>
          </el-dialog>
          <el-dialog :modal-append-to-body="false" :visible.sync="gradeVisible" title="Score"
          >
            <el-form :model="scoreForm" :rules="rules" label-position="top"
                     label-width="0px"
                     :ref="scoreForm"
                     style="margin: 20px auto;padding-bottom: 20px"
            >
              <el-form-item prop="score" style="margin: 20px" label="Score:">
                <el-rate v-model="scoreForm.score" :max="4" :colors="colors" show-text :texts="texts"></el-rate>
              </el-form-item>
              <el-form-item prop="comment" style="margin: 20px;" label="Comment:">
                <el-input type="textarea"
                          v-model="scoreForm.comment"
                          auto-complete="off"
                          placeholder="your comment here"
                          class="input"
                          maxlength="800"
                          :rows="3"
                          show-word-limit
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="confidence" style="margin: 20px" label="Confidence">
                <el-radio-group v-model="scoreForm.confidence">
                  <el-radio :label="'very low'">very low</el-radio>
                  <el-radio :label="'low'">low</el-radio>
                  <el-radio :label="'high'">high</el-radio>
                  <el-radio :label="'very high'">very high</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
            <el-button type="primary" @click="grade(scoreForm)">Grade</el-button>
          </el-dialog>

        </el-collapse-item>
      </el-collapse>
    </el-main>
    <el-footer style="text-align: center;height:10%;padding-top: 20px ">Copyright © 2020 还没名字</el-footer>
  </div>
</template>

<script>
  import pdf from "vue-pdf";

  export default {
    name: "PCMember",
    data() {
      const scoreValid = (rule, value, callback) => {
        if (!value || value === '' || value === 0) {
          return callback(new Error('Can\'t be empty'))
        }
        return callback()
      };
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
        scoreForm: {
          score: null,
          comment: '',
          confidence: '',
        },
        rules: {
          score: [{required: true, message: '', trigger: 'blur'}, {
            validator: scoreValid,
            message: 'Score is required',
            trigger: 'blur'
          }],
          comment: [{required: true, message: '', trigger: 'blur'}],
          confidence: [{required: true, message: '', trigger: 'blur'}],
        },
        colors: {2: '#99A9BF', 3: '#F7BA2A', 4: '#FF9900'},
        texts: ['-2:reject', '-1:weak reject', '1:weak accept', '2:accept'],
        theses: null,
        previewVisible: false,
        gradeVisible: false,
        src: '',
        pdfPage: 1,
        pageCount: 0,
        index: 0,
        pcAudit: null,
        rechanging1: false,
        rechanging2: false,
        stage: 0,
        rebuttal: null,
        rebuttalVisible: false,
        rebutting: false,
      }
    },
    components: {
      pdf
    },
    methods: {
      showRebuttal(index) {
        this.rebuttalVisible = true;
        this.rebuttal = this.theses[index].rebuttal;
      },
      goToForum(index) {

        this.$store.commit('setThesisID', this.theses[index].id);
        // console.log(this.$store.state.thesisID);
        this.$router.push('Discussion');
      }
      ,
      graded(index) {
        let pcAudits;
        for (let i = 0; i < 3; i++) {
          if (this.theses[index].pcAudits[i].username === this.$store.state.username) {
            pcAudits = this.theses[index].pcAudits[i];
          }
        }
        if (pcAudits === undefined) {
          return true;
        }
        if (this.stage === 0) {
          return pcAudits.audited;
        } else if (this.stage === 1) {
          return pcAudits.rechanged1;
        } else if (this.stage === 2) {
          return pcAudits.rechanged2;
        }
      },
      download(index) {
        let axios1 = require('axios');
        axios1({
          method: 'get',
          url: 'downloadThesis', params: {
            id: this.theses[index].id
          },
          responseType: 'blob'
        }).then(resp => {
          console.log(resp);
          const content = resp.data;
          const blob = new Blob([content], {type: 'application/pdf'});
          const fileName = this.theses[index].fileName + '.pdf';
          const eLink = document.createElement('a');
          eLink.download = fileName;
          eLink.style.display = 'none';
          eLink.href = URL.createObjectURL(blob);
          document.body.appendChild(eLink);
          eLink.click();
          URL.revokeObjectURL(eLink.href);
          document.body.removeChild(eLink);
        })
      }, openGrade(index) {
        this.gradeVisible = true;
        this.index = index;
        let pcAudits;
        for (let i = 0; i < 3; i++) {
          if (this.theses[index].pcAudits[i].username === this.$store.state.username) {
            pcAudits = this.theses[index].pcAudits[i];
          }
        }
        if (pcAudits !== undefined) {
          this.scoreForm.score = pcAudits.score;
          this.scoreForm.comment = pcAudits.comment;
          this.scoreForm.confidence = pcAudits.confidence;
        }
      }
      ,
      grade(formName) {
        console.log(this.scoreForm);
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.$axios.post('/auditThesis', {
              thesisID: this.theses[this.index].id,
              conferenceFullName: this.conferenceFullName,
              score: this.scoreForm.score,
              comment: this.scoreForm.comment,
              confidence: this.scoreForm.confidence,
              stage: this.stage
            }).then(resp => {
              this.$message({
                message: "successful gradation!",
                type: 'success'
              });
              location.reload();
            })

          }
        })
      }, preview(index) {
        this.previewVisible = true;
        let axios1 = require('axios');
        axios1({
          method: 'get',
          url: 'downloadThesis', params: {
            id: this.theses[index].id
          },
          responseType: 'blob'
        }).then(resp => {
          console.log(resp);
          this.src = window.webkitURL.createObjectURL(resp.data);
        });
      }, previousPage() {
        let p = this.pdfPage;
        p = p > 1 ? p - 1 : this.pageCount;
        this.pdfPage = p;
      }, nextPage() {
        let p = this.pdfPage;
        p = p < this.pageCount ? p + 1 : 1;
        this.pdfPage = p;
      },
      logout() {
        this.$message({
          message: 'Successful logout!',
          type: 'success'
        });
        this.$store.commit('logout');
        this.$router.replace('/login')
      },
    }, created() {
      this.$axios.post('/searchConference', {
        conferenceFullName: this.conferenceFullName
      })
        .then(resp => {
          console.log(resp);
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
          if (this.rechanging1) {
            this.stage = 1;
          } else if (this.rechanging2) {
            this.stage = 2;
          }
        });
      this.$axios.post('updateUser', {})
        .then(resp => {
          let length = resp.data.authorities.length;
          for (let i = 0; i < length; i++) {
            if (resp.data.authorities[i].conferenceFullName === this.$store.state.selectedConference.conference) {
              this.pcAudit = resp.data
            }
          }
        });
      if (this.$store.state.selectedConference.type === "chair") {
        this.$axios.post('/getAllTheses', {
          conferenceFullName: this.conferenceFullName
        }).then(resp => {
          this.theses = resp.data;
        })
      } else {
        this.$axios.post('pcGetTheses', {
          conferenceFullName: this.conferenceFullName
        }).then(resp => {
          this.theses = resp.data;
          console.log(this.theses);
        })
      }
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

  p {
    margin: 0;
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
