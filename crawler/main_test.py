import main


def test_get_page_source():
    main.get_page_source("https://warthunder.com/zh/community/userinfo/?nick=OnTheRocks", '//div[@class="content__title"]')