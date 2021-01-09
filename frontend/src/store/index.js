import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store(
  {
  state: {
    options:[{
      value: 'Africa',
      label: 'Africa',
      children: [{
        value: 'Central Africa',
        label: 'Central Africa',
        children: [
          {
            value: 'Cameroon',
            label: 'Cameroon'
          }, {
            value: 'Central African',
            label: 'Central African'
          }, {
            value: 'Chad',
            label: 'Chad'
          }, {
            value: 'Congo',
            label: 'Congo'
          }, {
            value: 'D.R.Congo',
            label: 'D.R.Congo'
          }, {
            value: 'Equatorial Guinea',
            label: 'Equatorial Guinea'
          }, {
            value: 'Gabon',
            label: 'Gabon'
          }
        ]
      }, {
        value: 'East Africa',
        label: 'East Africa',
        children: [
          {
            value: 'Burundi',
            label: 'Burundi'
          }, {
            value: 'Djibouti',
            label: 'Djibouti'
          }, {
            value: 'Eritrea',
            label: 'Eritrea'
          }, {
            value: 'Ethiopia',
            label: 'Ethiopia'
          }, {
            value: 'Kenya',
            label: 'Kenya'
          }, {
            value: 'Rwanda',
            label: 'Rwanda'
          }, {
            value: 'Seychelles',
            label: 'Seychelles'
          }, {
            value: 'Somalia',
            label: 'Somalia'
          }, {
            value: 'Tanzania',
            label: 'Tanzania'
          }, {
            value: 'Uganda',
            label: 'Uganda'
          }
        ]
      }, {
        value: 'North Africa',
        label: 'North Africa',
        children: [
          {
            value: 'Algeria',
            label: 'Algeria'
          }, {
            value: 'Egypt',
            label: 'Egypt'
          }, {
            value: 'Libya',
            label: 'Libya'
          }, {
            value: 'Morocco',
            label: 'Morocco'
          }, {
            value: 'Sudan',
            label: 'Sudan'
          }, {
            value: 'Tunisia',
            label: 'Tunisia'
          },
        ]
      }, {
        value: 'South Africa',
        label: 'South Africa',
        children: [
          {
            value: 'Angola',
            label: 'Angola'
          }, {
            value: 'Botswana',
            label: 'Botswana'
          }, {
            value: 'Comoros',
            label: 'Comoros'
          }, {
            value: 'Lesotho',
            label: 'Lesotho'
          }, {
            value: 'Madagascar',
            label: 'Madagascar'
          }, {
            value: 'Malawi',
            label: 'Malawi'
          }, {
            value: 'Mauritius',
            label: 'Mauritius'
          }, {
            value: 'Mozambique',
            label: 'Mozambique'
          }, {
            value: 'Namibia',
            label: 'Namibia'
          }, {
            value: 'South Africa',
            label: 'South Africa'
          }, {
            value: 'Swaziland',
            label: 'Swaziland'
          }, {
            value: 'Zambia',
            label: 'Zambia'
          }, {
            value: 'Zimbabwe',
            label: 'Zimbabwe'
          },
        ]
      }, {
        value: 'West Africa',
        label: 'West Africa',
        children: [
          {
            value: 'Benin',
            label: 'Benin'
          }, {
            value: 'Burkina Faso',
            label: 'Burkina Faso'
          }, {
            value: 'Cape Verde',
            label: 'Cape Verde'
          }, {
            value: 'Cote dIvoire',
            label: 'Cote dIvoire'
          }, {
            value: 'Gambia',
            label: 'Gambia'
          }, {
            value: 'Ghana',
            label: 'Ghana'
          }, {
            value: 'Guinea',
            label: 'Guinea'
          }, {
            value: 'Guinea-Bissau',
            label: 'Guinea-Bissau'
          }, {
            value: 'Liberia',
            label: 'Liberia'
          }, {
            value: 'Mali',
            label: 'Mali'
          }, {
            value: 'Mauritania',
            label: 'Mauritania'
          }, {
            value: 'Niger',
            label: 'Niger'
          }, {
            value: 'Senegal',
            label: 'Senegal'
          }, {
            value: 'Sierra Leone',
            label: 'Sierra Leone'
          }, {
            value: 'Togo',
            label: 'Togo'
          },
        ]
      }
      ]
    }, {
      value: 'Asia',
      label: 'Asia',
      children: [{
        value: 'Central Asia',
        label: 'Central Asia',
        children: [
          {
            value: 'Kazakhstan',
            label: 'Kazakhstan'
          }, {
            value: 'Kyrgyzstan',
            label: 'Kyrgyzstan'
          }, {
            value: 'Tajikistan',
            label: 'Tajikistan'
          }, {
            value: 'Turkmenistan',
            label: 'Turkmenistan'
          }, {
            value: 'Uzbekistan',
            label: 'Uzbekistan'
          }]
      }, {
        value: 'East Asia',
        label: 'East Asia',
        children: [
          {
            value: 'China',
            label: 'China'
          }, {
            value: 'Japan',
            label: 'Japan'
          }, {
            value: 'Mongolia',
            label: 'Mongolia'
          }, {
            value: 'North Korea',
            label: 'North Korea'
          }, {
            value: 'South Korea',
            label: 'South Korea'
          }]
      }, {
        value: 'Southeast Asia',
        label: 'Southeast Asia',
        children: [
          {
            value: 'Brunei',
            label: 'Brunei'
          }, {
            value: 'Cambodia',
            label: 'Cambodia'
          }, {
            value: 'East Timor',
            label: 'East Timor'
          }, {
            value: 'Indonesia',
            label: 'Indonesia'
          }, {
            value: 'Laos',
            label: 'Laos'
          }, {
            value: 'Malaysia',
            label: 'Malaysia'
          }, {
            value: 'Myanmar',
            label: 'Myanmar'
          }, {
            value: 'Philippines',
            label: 'Philippines'
          }, {
            value: 'Singapore',
            label: 'Singapore'
          }, {
            value: 'Vietnam',
            label: 'Vietnam'
          },
        ]
      }, {
        value: 'South Asia',
        label: 'South Asia',
        children: [
          {
            value: 'Bangladesh',
            label: 'Bangladesh'
          }, {
            value: 'Bhutan',
            label: 'Bhutan'
          }, {
            value: 'India',
            label: 'India'
          }, {
            value: 'Maldives',
            label: 'Maldives'
          }, {
            value: 'Nepal',
            label: 'Nepal'
          }, {
            value: 'Pakistan',
            label: 'Pakistan'
          }, {
            value: 'Sikkim',
            label: 'Sikkim'
          }, {
            value: 'Sri Lanka',
            label: 'Sri Lanka'
          }]
      }, {
        value: 'West Asia',
        label: 'West Asia',
        children: [
          {
            value: 'Afghanistan',
            label: 'Afghanistan'
          }, {
            value: 'Armenia',
            label: 'Armenia'
          }, {
            value: 'Azerbaijan',
            label: 'Azerbaijan'
          }, {
            value: 'Bahrain',
            label: 'Bahrain'
          }, {
            value: 'Cyprus',
            label: 'Cyprus'
          }, {
            value: 'Georgia',
            label: 'Georgia'
          }, {
            value: 'Iran',
            label: 'Iran'
          }, {
            value: 'Iraq',
            label: 'Iraq'
          }, {
            value: 'Israel',
            label: 'Israel'
          }, {
            value: 'Jordan',
            label: 'Jordan'
          }, {
            value: 'Kuwait',
            label: 'Kuwait'
          }, {
            value: 'Lebanese',
            label: 'Lebanese'
          }, {
            value: 'Oman',
            label: 'Oman'
          }, {
            value: 'Palestine',
            label: 'Palestine'
          }, {
            value: 'Qatar',
            label: 'Qatar'
          }, {
            value: 'Saudi Arabia',
            label: 'Saudi Arabia'
          }, {
            value: 'Syrian',
            label: 'Syrian'
          }, {
            value: 'Turkey',
            label: 'Turkey'
          }, {
            value: 'United Arab Emirates',
            label: 'United Arab Emirates'
          }, {
            value: 'Yemen',
            label: 'Yemen'
          },
        ]
      }
      ]
    }, {
      value: 'Europe',
      label: 'Europe',
      children: [{
        value: 'Central Europe',
        label: 'Central Europe',
        children: [
          {
            value: 'Austria',
            label: 'Austria'
          }, {
            value: 'Czech',
            label: 'Czech'
          }, {
            value: 'Germany',
            label: 'Germany'
          }, {
            value: 'Hungary',
            label: 'Hungary'
          }, {
            value: 'Liechtenstein',
            label: 'Liechtenstein'
          }, {
            value: 'Poland',
            label: 'Poland'
          }, {
            value: 'Slovakia',
            label: 'Slovakia'
          }, {
            value: 'Slovenia',
            label: 'Slovenia'
          }, {
            value: 'Switzerland',
            label: 'Switzerland'
          },]
      }, {
        value: 'East Europe',
        label: 'East Europe',
        children: [
          {
            value: 'Belarus',
            label: 'Belarus'
          }, {
            value: 'Estonia',
            label: 'Estonia'
          }, {
            value: 'Latvia',
            label: 'Latvia'
          }, {
            value: 'Lithuania',
            label: 'Lithuania'
          }, {
            value: 'Moldova',
            label: 'Moldova'
          }, {
            value: 'Russia',
            label: 'Russia'
          }, {
            value: 'Ukraine',
            label: 'Ukraine'
          },]
      }, {
        value: 'North Europe',
        label: 'North Europe',
        children: [
          {
            value: 'Denmark',
            label: 'Denmark'
          }, {
            value: 'Finland',
            label: 'Finland'
          }, {
            value: 'Iceland',
            label: 'Iceland'
          }, {
            value: 'Norway',
            label: 'Norway'
          }, {
            value: 'Sweden',
            label: 'Sweden'
          }
        ]
      }, {
        value: 'South Europe',
        label: 'South Europe',
        children: [
          {
            value: 'Albania',
            label: 'Albania'
          }, {
            value: 'Andorra',
            label: 'Andorra'
          }, {
            value: 'Bosnia and Herzegovina',
            label: 'Bosnia and Herzegovina'
          }, {
            value: 'Bulgaria',
            label: 'Bulgaria'
          }, {
            value: 'Croatia',
            label: 'Croatia'
          }, {
            value: 'Greece',
            label: 'Greece'
          }, {
            value: 'Italy',
            label: 'Italy'
          }, {
            value: 'Macedonia',
            label: 'Macedonia'
          }, {
            value: 'Malta',
            label: 'Malta'
          }, {
            value: 'Montenegro',
            label: 'Montenegro'
          }, {
            value: 'Portugal',
            label: 'Portugal'
          }, {
            value: 'Romania',
            label: 'Romania'
          }, {
            value: 'San Marino',
            label: 'San Marino'
          }, {
            value: 'Serbia',
            label: 'Serbia'
          }, {
            value: 'Spain',
            label: 'Spain'
          }, {
            value: 'Vatican',
            label: 'Vatican'
          }]
      }, {
        value: 'West Europe',
        label: 'West Europe',
        children: [
          {
            value: 'Belgium',
            label: 'Belgium'
          }, {
            value: 'France',
            label: 'France'
          }, {
            value: 'Ireland',
            label: 'Ireland'
          }, {
            value: 'Luxembourg',
            label: 'Luxembourg'
          }, {
            value: 'Monaco',
            label: 'Monaco'
          }, {
            value: 'Netherlands',
            label: 'Netherlands'
          }
        ]
      }]
    }, {
      value: 'North America',
      label: 'North America',
      children: [{
        value: 'America',
        label: 'America',
      }, {
        value: 'Bahamas',
        label: 'Bahamas',
      }, {
        value: 'Belize',
        label: 'Belize'
      }, {
        value: 'Canada',
        label: 'Canada'
      }, {
        value: 'Cuba',
        label: 'Cuba'
      }, {
        value: 'Costa Rica',
        label: 'Costa Rica'
      }, {
        value: 'Dominican',
        label: 'Dominican'
      }, {
        value: 'Haiti',
        label: 'Haiti'
      }, {
        value: 'Honduras',
        label: 'Honduras'
      }, {
        value: 'Jamaica',
        label: 'Jamaica'
      }, {
        value: 'Mexico',
        label: 'Mexico'
      }, {
        value: 'Nicaragua',
        label: 'Nicaragua'
      }, {
        value: 'Panama',
        label: 'Panama'
      }, {
        value: 'Salvador',
        label: 'Salvador'
      },]
    }, {
      value: 'Oceania',
      label: 'Oceania',
      children: [{
        value: 'Australia',
        label: 'Australia',
      }, {
        value: 'Cook Islands',
        label: 'Cook Islands'
      }, {
        value: 'Fiji',
        label: 'Fiji',
      }, {
        value: 'Kiribati',
        label: 'Kiribati'
      }, {
        value: 'Mashall Islands',
        label: 'Mashall Islands'
      }, {
        value: 'Micronesia',
        label: 'Micronesia'
      }, {
        value: 'Nauru',
        label: 'Nauru'
      }, {
        value: 'New Zealand',
        label: 'New Zealand'
      }, {
        value: 'Niue',
        label: 'Niue'
      }, {
        value: 'Palau',
        label: 'Palau'
      }, {
        value: 'Papua New Guinea',
        label: 'Papua New Guinea'
      }, {
        value: 'Samoa',
        label: 'Samoa'
      }, {
        value: 'Solomon Islands',
        label: 'Solomon Islands'
      }, {
        value: 'Tonga',
        label: 'Tonga'
      }, {
        value: 'Tuvalu',
        label: 'Tuvalu'
      }, {
        value: 'Vanuatu',
        label: 'Vanuatu'
      },]
    }, {
      value: 'South America',
      label: 'South America',
      children: [{
        value: 'Argentina',
        label: 'Argentina',
      }, {
        value: 'Bolivia',
        label: 'Bolivia',
      }, {
        value: 'Brazil',
        label: 'Brazil'
      }, {
        value: 'Chile',
        label: 'Chile'
      }, {
        value: 'Colombia',
        label: 'Colombia'
      }, {
        value: 'Ecuador',
        label: 'Ecuador'
      }, {
        value: 'Guyana',
        label: 'Guyana'
      }, {
        value: 'Paraguay',
        label: 'Paraguay'
      }, {
        value: 'Peru',
        label: 'Peru'
      }, {
        value: 'Suriname',
        label: 'Suriname'
      }, {
        value: 'Uruguay',
        label: 'Uruguay'
      }, {
        value: 'Venezuela',
        label: 'Venezuela'
      },]
    },],
    admin:localStorage.getItem('admin') || false,
    token: localStorage.getItem('token') || null,
    username:localStorage.getItem('username')||null,
    fullName:localStorage.getItem('fullName')||null,
    email:localStorage.getItem('email'||null),
    office:localStorage.getItem('office')||null,
    region:localStorage.getItem('region')||null,
    selectedConference:JSON.parse(localStorage.getItem('selectedConference'))||null,
  //  authorities:JSON.parse(localStorage.getItem('authorities'))||null,
    conferences:JSON.parse(localStorage.getItem('conferences'))||null,
    thesisID:localStorage.getItem('thesisID')||null,
    authorities:JSON.parse(localStorage.getItem('authorities'))||null,
    information:localStorage.getItem('information')||null



    //测试拦截的预设token
    // token:1,
    // userDetails:2
  },
  mutations: {
    //测试拦截的预设login
    // login(state){
    //   state.userDetails+=1
    //   state.token+=1
    //     },
    login(state, data){
      localStorage.setItem('token', data.token);
      state.token = data.token
    },
    setUserDetails(state,data){
      // localStorage.setItem('username',data.username);
      // localStorage.setItem('email',data.email);
      // localStorage.setItem('office',data.office);
      // localStorage.setItem('fullName',data.fullName);
      // localStorage.setItem('region',data.region[data.region.length-1]);
      // state.username=data.username;
      // state.email=data.email;
      // state.office=data.office;
      // state.fullName=data.fullName;
      // state.region=data.region[data.region.length-1];
      localStorage.setItem('authorities',JSON.stringify(data.authorities));
      state.authorities=data.authorities;
      localStorage.setItem('information',data.information);
      state.information=data.information;
      localStorage.setItem('username',data.username);
      state.username=data.username
    },
    logout(state) {
      // 移除token
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('email');
      localStorage.removeItem('office');
      localStorage.removeItem('admin');
      localStorage.removeItem('selectedConference');
      localStorage.removeItem('conferences');
      localStorage.removeItem('fullName');
      localStorage.removeItem('region');
      localStorage.removeItem('thesisID');
     // localStorage.removeItem('authorities');
      state.token = null;
      state.username = null;
      state.email=null;
      state.office=null;
      state.admin=false;
      state.selectedConference=null;
      state.conferences=null;
      state.region=null;
      state.fullName=null;
      state.thesisID=null;
     // state.authorities=null;
    },
    //type:String  chair:1  PC:2 author:3 contribution:4
    setSelectedConference(state,data){
     var tep={conference:data.conference,type:data.type};
    localStorage.setItem('selectedConference',JSON.stringify(tep));
      state.selectedConference=tep;
    },
    removeSelectedConference(state){
      localStorage.removeItem('selectedConference');
      state.selectedConference=null;
    },
    setConferences(state,data){
      state.conferences=data;
      localStorage.setItem('conferences',JSON.stringify(data));
    },
    removeConferences(state){
      state.conferences=null;
      localStorage.removeItem('conferences');
    },
    setAdmin(state){
      localStorage.setItem('admin',true)
      state.admin=true;
    },
    setThesisID(state,data){
      localStorage.setItem('thesisID',data);
      state.thesisID=data;
    },
    removeThesisID(state){
      state.thesisID=null;
      localStorage.removeItem('thesisID');
    }
    // setAuthorities(state,data){
    //   state.authorities=data.authorities;
    //   localStorage.setItem('authorities',JSON.stringify(data.authorities));
    // },
    // removeAuthorities(state){
    //   state.authorities=null;
    //   localStorage.removeItem('authorities');
    // },

    // removeAdmin(state){
    //   localStorage.removeItem('admin');
    //   state.admin=false;
    // },



    //测试拦截的logout
    // logout(state){
    // state.userDetails=null
    // state.token=null
    // }
  },
  actions: {
  }
})
