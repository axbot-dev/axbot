from driver import get_page_html


def test_get_page_html():
    page = get_page_html("https://warthunder.com/zh/community/userinfo/?nick=OnTheRocks",
                         '//div[@class="content__title"]')
    assert page != ""
