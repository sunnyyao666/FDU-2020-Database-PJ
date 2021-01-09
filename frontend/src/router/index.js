import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Login from '@/components/Login'
import Register from '@/components/Register'
import PersonalInformation from "../components/PersonalInformation"
import ManagerAssessment from "../components/ManagerAssessment"
import MeetingManagement from "../components/MeetingManagement"
import store from '../store'
import Application from '@/components/Application'
import Chair from "../components/Chair";
import Contribution from "../components/Contribution";
import PCMember from "../components/PCMember";
import Author from "../components/Author";
import Discussion from "../components/Discussion";
import Doctor from "../components/Doctor";
import EmergencyNurse from "../components/EmergencyNurse";
import Matron from "../components/Matron";
import WardNurse from "../components/WardNurse";
import Information from "../components/Information";
import {Message} from 'element-ui';

Vue.use(Router);

export const router = new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      redirect: '/login',
      component: HelloWorld,
      meta: {
        // requireAuth: true // 需要登录权限
      },
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      // meta: {
      //   tag: "logged"
      // }
    },
    {
      path: '/Doctor',
      name: 'Doctor',
      component: Doctor,
      // meta:{
      //   tag:"logged"
      // }
    }, {
      path: '/EmergencyNurse',
      name: 'EmergencyNurse',
      component: EmergencyNurse,
      // meta:{
      //   tag:"logged"
      // }
    }, {
      path: '/Matron',
      name: 'Matron',
      component: Matron,
    }, {path: '/WardNurse', name: 'WardNurse', component: WardNurse},{
    path:'/information',
      name:'information',
      component:Information
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      // meta: {
      //   tag: "logged"
      // }
    },
    //   {
    //     path: '/personalInformation',
    //     name: 'PersonalInformation',
    //     component: PersonalInformation,
    //     meta: {
    //       requireAuth: true
    //     },
    //   },
    //   {
    //     path: '/application',
    //     name: 'Application',
    //     component: Application,
    //     meta: {
    //       requireAuth: true // 需要登录权限
    //     }
    //   },
    //   {
    //     path: '/ManagerAssessment',
    //     name: 'ManagerAssessment',
    //     component: ManagerAssessment,
    //     meta: {
    //       tag: 'admin'
    //     },
    //     beforeEnter: (to, from, next) => {
    //       if (!store.state.admin) {
    //         Message({
    //           message: "You haven't the authority",
    //           type: "error",
    //           showClose: true,
    //           duration: 0,
    //         });
    //         router.go(-1)
    //       //  next(false)
    //       } else next()
    //     }
    //   }, {
    //     path: '/chair',
    //     name: 'Chair',
    //     component: Chair,
    //     meta: {
    //       requireAuth: true,
    //       tag: 'selectedConference'
    //     },
    //     beforeEnter: (to, from, next) => {
    //       if (!store.state.selectedConference||store.state.selectedConference.type!=='chair') {
    //
    //         Message({
    //           message:"invalid access" ,
    //           type: "error",
    //           showClose: true,
    //           duration: 0,
    //         });
    //         router.go(-1)
    //        // next(false)
    //       } else next()
    //     }
    //
    //   },
    //   {
    //     path: '/MeetingManagement',
    //     name: 'MeetingManagement',
    //     component: MeetingManagement,
    //     meta: {
    //       requireAuth: true
    //     },
    //   }, {
    //     path: '/contribution',
    //     name: 'Contribution',
    //     component: Contribution,
    //     meta: {
    //       requireAuth: true,
    //       tag: 'selectedConference'
    //     },
    //     beforeEnter: (to, from, next) => {
    //       if (!store.state.selectedConference||(store.state.selectedConference.type!=='contribution'&&store.state.selectedConference.type!=='author')) {
    //         Message({
    //           message: "invalid access",
    //           type: "error",
    //           showClose: true,
    //           duration: 0,
    //         });
    //         router.go(-1)
    //         //next(false)
    //       } else next()
    //     }
    //   }, {
    //     path: '/pcmember',
    //     name: 'PCMember',
    //     component: PCMember,
    //     meta: {
    //       requireAuth: true,
    //       tag: 'selectedConference'
    //     },
    //     beforeEnter: (to, from, next) => {
    //       if (!store.state.selectedConference||(store.state.selectedConference.type!=='PC'&&store.state.selectedConference.type!=='chair')) {
    //         Message({
    //           message:"invalid access",
    //           type: "error",
    //           showClose: true,
    //           duration: 0,
    //         });
    //        router.go(-1)
    //        // next(false)
    //       } else next()
    //     }
    //   },{
    //     path: '/author',
    //     name: 'Author',
    //     component: Author,
    //     meta: {
    //       requireAuth: true,
    //       tag: 'selectedConference'
    //     },
    //     beforeEnter: (to, from, next) => {
    //       if (!store.state.selectedConference||store.state.selectedConference.type!=='author') {
    //         Message({
    //           message: "invalid access",
    //           type: "error",
    //           showClose: true,
    //           duration: 0,
    //         });
    //         router.go(-1)
    //        // next(false)
    //       } else next()
    //     }
    //   },{
    //     path:'/discussion',
    //     name:'discussion',
    //     component:Discussion,
    //     meta:{
    //       //requireAuth:true,
    //       tag: 'selectedConference',
    //       thesis:'thesis'
    //
    //     },
    //     beforeEnter:(to,from,next)=>{
    //       if(!store.state.thesisID){
    //         Message({
    //           message: "invalid access",
    //           type: "error",
    //           showClose: true,
    //           duration: 0,
    //         });
    //         router.go(-1)
    //       } else next()
    //     }
    //   }
  ]
})

// 前端登录拦截
router.beforeEach(function (to, from, next) {
  if (to.meta.tag !== 'selectedConference' && store.state.selectedConference) {
    store.commit('removeSelectedConference')

  }
  if (to.meta.thesis !== 'thesis' && store.state.thesisID) {
    store.commit('removeThesisID')
  }

  if (to.meta.tag !== 'admin' && store.state.admin) {
    Message({
      message: 'Administrator Model',
      type: 'warning',
      showClose: true,
      duration: 0,
    });
    //router.go(-1)
    next({path: '/ManagerAssessment',})
  } else if (to.matched.some(record => record.meta.requireAuth)) {
    // else if(to.meta.requireAuth){
    if (store.state.token) {
      next()

    } else {
      Message({
        message: "no login information",
        type: "error",
        showClose: true,
        duration: 0,
      });
      next({
        path: '/login',
        query: {redirect: to.fullPath} // 登录成功之后重新跳转到该路由
      })
    }
  } else if (to.meta.tag === 'logged') {
    if (store.state.token) {
      //测试meta属性的添加
      //console.log(to.meta.title)
      Message({
        message: "Already logged in.",

        showClose: true,
        duration: 0,
      });
      // store.commit("logout");
      router.replace('/personalInformation')
      // next({
      //   path: '/personalInformation'
      // });
    } else {
      //console.log("no token")
      next();
    }
  } else {
    next()
  }
})
