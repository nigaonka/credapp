from hamcrest import assert_that, equal_to


class TestEchoService:
    """
    This module shows how to use assertion, logging functionality.
    """

    def test_echo_service_endpoint(self):
        # logger comes from conftest.py defined in before_class
        self.logger.log_and_print_step("\n======= Starting executing test case =======")

        params = {'type': 'ping', 'response': {'type': 'success'}}
        # echo_base comes from conftest.py defined in before_class
        response = self.echo_base.ping_echo(req_body=params)
        assert_that(response[ 'type' ], equal_to(params[ 'response' ][ 'type' ]),
                    f"Response type mismatch, Expected: {params[ 'response' ][ 'type' ]} but Actual: {response[ 'type' ]}")
        # assertion comes from conftest.py defined in before_class
        self.assertion.verify_resp_json_must_have_key(response, ['type'])

        self.logger.log_and_print_step("\n======= Finished running test case =======")
