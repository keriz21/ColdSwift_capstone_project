// utils.js
async function loadDateFormat() {
    const dateFormatModule = await import('dateformat');
    return dateFormatModule.default;
  }
  
  module.exports = { loadDateFormat };
  