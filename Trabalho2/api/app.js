const express = require('express')
const app = express()

app.listen(8080, () => {
  console.log('server iniciado: http://localhost:8080')
})

// app.use(express.json())

app.get('/', (req, res) => {
  let html = ''
  html += 'SEL0612 - ONDAS ELETROMAGNETICAS'
  return res.send(html)
})




