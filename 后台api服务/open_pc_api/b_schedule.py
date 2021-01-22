import pytz
import requests
from apscheduler.executors.pool import ThreadPoolExecutor,ProcessPoolExecutor
from apscheduler.schedulers.background import BackgroundScheduler

from u_util import *
from z_global import ip, port


def 推送开机信息(id = None):
    try:
        if id:
            print(id)
            requests.get(f"http://{ip}:{port}/dev/open?id="+id)
    except:
        pass

def get_new_scheduler():
    return BackgroundScheduler(timezone=pytz.timezone('Asia/Shanghai'))

@线程模式
def 启动定时任务():
    scheduler = get_new_scheduler()
    cron_conf = None
    while True:
        try:
            dal = mysql()
            sql = f'''
                    select dev_id id, value cron from cron_job_conf where state=1 and type='开机' ;
            '''
            lines = dal.exec(sql).lines
            new_conf = to_json_str(lines)
            if new_conf != cron_conf:
                print(to_now_时间字符串() + " 配置文件发生变动")
                cron_conf = new_conf
                try:
                    scheduler.shutdown()
                except:
                    pass
                scheduler = get_new_scheduler()
                print(lines)
                for l in lines:
                    print(l)
                    id = l.get("id")
                    cron = to_json_obj(l.get("cron"))
                    args = [id, ]
                    scheduler.add_job(推送开机信息, 'cron', args=args,
                                      year=cron.get("year"), month=cron.get("month"), day=cron.get("day"),
                                      week=cron.get("week"), day_of_week=cron.get("day_of_week"),
                                      hour=cron.get("hour"), minute=cron.get("minute"), second=cron.get("second"))
                scheduler.start()
        except Exception as e:
            print(to_now_时间字符串() + " 出错")
            print(e)
        print(to_now_时间字符串() + " 检查结束")
        time.sleep(600)


if __name__ == '__main__':
    启动定时任务()