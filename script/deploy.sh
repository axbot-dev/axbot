#!/bin/bash

if [ "$1" = "help" ]; then
    echo "这是一个帮助信息"
    echo "用法: script [command]"
    echo "命令列表:"
    echo "  help    显示帮助信息"
    echo "  update  执行更新操作"
elif [ "$1" = "update" ]; then
    echo "正在执行更新操作..."
    # 在这里编写实际的更新操作
    echo "更新完成！"
else
    echo "未知命令，请输入 'script help' 查看帮助信息。"
fi
