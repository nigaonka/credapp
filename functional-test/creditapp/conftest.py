import pytest

from apitestlib.core.api.assertion import ApiAssertion
from apitestlib.conftest import pytest_addoption as addoption
from apitestlib.conftest import pytest_configure as configure
from tests.echobase import EchoBase

# https://docs.pytest.org/en/6.2.x/fixture.html#using-fixtures-from-other-projects
# using fixtures from - from apitestlib.fixtures
pytest_plugins  = "apitestlib.fixtures"


def pytest_addoption(parser):
    addoption(parser)


def pytest_configure(config):
    configure(config)

@pytest.fixture(scope='class', autouse=True)
def before_class(request):
    """
    Configure steps to be executed before the test cases run
    """
    # Setting class level variables, these can be referenced
    # from any test modules with self.<variableName>
    request.cls.echo_base = EchoBase()
    request.cls.assertion = ApiAssertion()
