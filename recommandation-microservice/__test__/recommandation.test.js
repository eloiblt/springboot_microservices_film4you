const supertest = require('supertest');
const app = require('../index.js');

describe("GET /public/ping", () => {

	it("tests the ping route and returns pong for status", async () => {

		const response = await supertest(app).get('/public/ping');

		expect(response.status).toBe(200);
		expect(response.body.message).toBe('pong');
        expect(response.body).toHaveProperty('port');
	});

});