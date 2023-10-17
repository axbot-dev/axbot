from DrissionPage import ChromiumPage
from DrissionPage.easy_set import set_paths
def config_browser_path(path: str):
    set_paths(browser_path=r'/usr/bin/chromedriver')

def get_page_html(url: str, wait_xpath: str) -> str:
    """使用DrissionPage获取网页的html内容

    :param url: 网页地址
    :param wait_xpath: 等待的xpath条件
    :return:
    """
    page = ChromiumPage()
    page.get(url, show_errmsg=True, timeout=60)
    page.wait.ele_display(wait_xpath)
    html_str = page.html
    page.quit()
    return html_str
