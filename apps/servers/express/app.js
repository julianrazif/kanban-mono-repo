const express = require('express');
const cors = require('cors');
const logger = require('morgan');

const app = express();
app.port = process.env.PORT || 3000;

app.use(cors());
app.use(logger('dev'));

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.listen(app.port, () => console.log(`Server listening on port ${app.port}`));
