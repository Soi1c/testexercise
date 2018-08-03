export const MASKS = {

  date: {
    guide: false,
    keepCharPositions: true,
    mask: [/\d/, /\d/, '.', /\d/, /\d/, '.',/\d/, /\d/, /\d/, /\d/ ]
  },
  passportCode: {
    guide: false,
    keepCharPositions: true,
    mask: [/[0-9]/, /[0-9]/, /[0-9]/, '-', /[0-9]/, /[0-9]/, /[0-9]/]
  },
  phone: {
    guide: false,
    keepCharPositions: true,
    mask: ['+', '7', ' ', /[1-9]/, /\d/, /\d/, ' ', /\d/, /\d/, /\d/, /\d/, /\d/, /\d/, /\d/]
  },

};
