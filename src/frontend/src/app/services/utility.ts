import * as moment from 'moment';


const ERRORS_LIST = {
  ['Invalid password']: 'Неверный пароль',
  ['Unable find account in database']: 'Не удалось найти пользователя'
};

export class UtilityService {

  static returnErrorMessage(description: string): string {
    return ERRORS_LIST[description] || description;
  }

  static dateToString(date: string) {
    return moment(date).format('YYYY-MM-DD');
  }

  static stringToDate(string?: string) {
    if (!!string) {
      const [year, month, day] = string.split('-');
      return moment(`${year}-${month}-${day}`).toDate();
    }
  }


  static capitalizeFirst(string: string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

  static guid() {
    const s4 = () => Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
      s4() + '-' + s4() + s4() + s4();
  }
}
