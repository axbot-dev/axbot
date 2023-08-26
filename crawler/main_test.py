import main


def test_get_page_source():
    page = main.get_page_source("https://warthunder.com/zh/community/userinfo/?nick=OnTheRocks",
                                '//div[@class="content__title"]')
    assert page != ""
