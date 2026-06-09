#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Shiro到Sa-Token注解批量替换脚本
用于批量替换Controller中的Shiro注解为Sa-Token注解
"""

import os
import re

def replace_annotations_in_file(file_path):
    """替换单个文件中的注解"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        modified = False
        
        # 替换导入语句
        if 'import org.apache.shiro.authz.annotation.RequiresPermissions;' in content:
            content = content.replace(
                'import org.apache.shiro.authz.annotation.RequiresPermissions;',
                'import cn.dev33.satoken.annotation.SaCheckPermission;'
            )
            modified = True
        
        if 'import org.apache.shiro.authz.annotation.RequiresRoles;' in content:
            content = content.replace(
                'import org.apache.shiro.authz.annotation.RequiresRoles;',
                'import cn.dev33.satoken.annotation.SaCheckRole;'
            )
            modified = True
        
        if 'import org.apache.shiro.authz.annotation.Logical;' in content:
            content = content.replace(
                'import org.apache.shiro.authz.annotation.Logical;',
                'import cn.dev33.satoken.annotation.SaMode;'
            )
            modified = True
        
        # 替换注解
        content = re.sub(r'@RequiresPermissions\b', '@SaCheckPermission', content)
        content = re.sub(r'@RequiresRoles\b', '@SaCheckRole', content)
        
        # 替换logical参数
        content = content.replace('logical = Logical.AND', 'mode = SaMode.AND')
        content = content.replace('logical = Logical.OR', 'mode = SaMode.OR')
        
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f'[OK] Processed: {file_path}')
            return True
        return False
        
    except Exception as e:
        print(f'[ERROR] Failed {file_path}: {str(e)}')
        return False

def process_directory(directory):
    """递归处理目录下所有Java文件"""
    count = 0
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if replace_annotations_in_file(file_path):
                    count += 1
    return count

if __name__ == '__main__':
    # Controller目录
    controller_dir = 'ruoyi-admin/src/main/java/com/ruoyi/web/controller'
    
    # Quartz Controller目录
    quartz_controller_dir = 'ruoyi-quartz/src/main/java/com/ruoyi/quartz/controller'
    
    # Generator Controller目录  
    generator_controller_dir = 'ruoyi-generator/src/main/java/com/ruoyi/generator/controller'
    
    print('=' * 60)
    print('Shiro到Sa-Token注解批量替换工具')
    print('=' * 60)
    print()
    
    total_count = 0
    
    if os.path.exists(controller_dir):
        print(f'处理目录: {controller_dir}')
        count = process_directory(controller_dir)
        total_count += count
        print(f'该目录处理文件数: {count}')
        print()
    
    if os.path.exists(quartz_controller_dir):
        print(f'处理目录: {quartz_controller_dir}')
        count = process_directory(quartz_controller_dir)
        total_count += count
        print(f'该目录处理文件数: {count}')
        print()
    
    if os.path.exists(generator_controller_dir):
        print(f'处理目录: {generator_controller_dir}')
        count = process_directory(generator_controller_dir)
        total_count += count
        print(f'该目录处理文件数: {count}')
        print()
    
    print('=' * 60)
    print(f'总计处理文件数: {total_count}')
    print('注解替换完成！')
    print('=' * 60)
