<template>
    <div>
        <div class="app-container">
            <!-- 查询命令选择 -->
            <el-card
                class="box-card"
                body-style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)"
                shadow="hover"
            >
                <div slot="header" class="clearfix">查询条件</div>
            
                <el-form :inline="true" size="small">

                    <el-form-item>
                        <el-input
                            v-model="title"
                            placeholder="请输入电影asin"
                            clearable
                            suffix-icon="el-icon-edit"
                        />
                    </el-form-item>
                    <el-form-item>
                        <el-button
                            icon="el-icon-search"
                            plain
                            @click="handleQuery"
                        />
                    </el-form-item>
                </el-form>
                <br />
            </el-card>
            <br />
            <!-- movie查询结果展示 -->
            <el-card
                class="box-card"
                body-style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)"
                shadow="hover"
            >
                <div slot="header" class="clearfix">
                    <span>查询结果</span>
                </div>
                <el-table :data="results">
                    <el-table-column prop="id" label="电影id" align="center" />
                </el-table>
            </el-card>
        </div>
    </div>
</template>

<script>
import { showLoading, hideLoading } from "../../utils/loading"
import { get } from "js-cookie"
import {
    getByAsin
} from "../../api/query"

export default {
    name: "Moviesource",
    data() {
        return {
            title: "Comedy",
            results: [],
        }
    },
    methods: {
        //提交查询
        handleQuery: function() {
            showLoading()
            this.$data.results = []
            console.log(this.$data.field)
            const para = {
                asinn: this.$data.title
            }
            getByAsin(para).then(
                response => {
                    console.log("haskjhdaskhkjashfkj")
                    console.log(response.data)
                    let movieInfo = response.data
                    console.log(movieInfo)
                    this.getMobieList(movieInfo)
                    console.log(this.$data.results)
                },
                error => {
                    this.$message({
                        message: "服务器连接失败",
                        type: "error"
                    })
                    hideLoading()
                    return
                }
            )
            hideLoading()
        },
        //把得到的数据转成movieList
        getMobieList: function(movieInfo) {
            for (var key in movieInfo) {
                this.$data.results.push({
                    id: movieInfo[key],
                })
            }
        },
    }
}
</script>

<style scoped></style>
