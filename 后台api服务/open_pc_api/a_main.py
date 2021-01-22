import redis as redis
from flask import Flask, jsonify, request, abort
import py_eureka_client.eureka_client as eureka_client

from b_schedule import 启动定时任务
from u_util import *
from z_global import 应用名称, ip, port, redis_open_pc_key, redis_shutdown_pc_key




if __name__ == '__main__':

    # -- flask初始化设置
    app = Flask(__name__)
    # 使用jsonify方法时需要添加一句 app.config['JSON_AS_ASCII'] = False
    # 这样做可以避免显示时中文乱码。
    app.config['JSON_AS_ASCII'] = False
    app.config["DEBUG"] = False
    app.config['MAX_CONTENT_LENGTH'] = 100 * 1024 * 1024


    # -- 连接redis
    r = redis.Redis(host="127.0.0.1", port=35004, password="123456")


    #region -- flask控制器
    # 例：http://127.0.0.1:45032/dev/open?id=a1
    @app.route('/dev/open', methods=['GET','POST'])
    def 开机():
        rst = {
            "code": 200,
            "msg": "ok",
            "data": None,
        }
        try:
            # 获取参数
            id = request.values.get("id")
            if not id:
                id = getDictValue(request.json, "id")
            if not id:
                raise Exception("未找到id参数")
            # 发送数据到redis
            r.lpush(redis_open_pc_key, id)
        except Exception as e:
            rst["code"] = 210
            rst["msg"] = str(e)

        return jsonify(rst)


    @app.route('/dev/shutdown')
    def 关机():
        rst = {
            "code": 200,
            "msg": "ok",
            "data": None,
        }
        try:
            # 获取参数
            id = request.values.get("id")
            if not id:
                id = getDictValue(request.json, "id")
            if not id:
                raise Exception("未找到id参数")
            # 发送数据到redis
            r.lpush(redis_shutdown_pc_key, id)
        except Exception as e:
            rst["code"] = 210
            rst["msg"] = str(e)

        return jsonify(rst)
    #endregion


    启动定时任务()

    # -- 启动api应用
    app.run("0.0.0.0", port)  # host port